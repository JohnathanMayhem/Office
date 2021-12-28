package com.example.officemanagerapp.ui.chat.singlechat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.style.TtsSpan.TYPE_TEXT
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.officemanagerapp.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_single_chat.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.officemanagerapp.ui.MainActivity
import com.example.officemanagerapp.ui.chat.Chat
import com.example.officemanagerapp.ui.chat.ChatFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage


class SingleChatFragment: Fragment(R.layout.fragment_single_chat) {
    private val args by navArgs<SingleChatFragmentArgs>()

    private lateinit var mToolbarInfo: View

    private lateinit var mRefChat: DatabaseReference
    private lateinit var refStoreg: StorageReference

    private lateinit var mRefMesseges: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var messageRecView: RecyclerView
    private lateinit var messageListener: AppChildEventListener
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private val userUid = Firebase.auth.currentUser!!.uid.toString()

    private var mListMessage = mutableListOf<Message>()
    private var mCountMessages = 10;
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true
    //private var mListListeners = mutableListOf<AppChildEventListener>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInfoToolBar()
        initFields()
        initRecView()
        chat_btn_send_message.setOnClickListener {
            mSmoothScrollToPosition = true
            val message = chat_input_message.text.toString()
            if (message.isEmpty()){
                val toast = Toast.makeText(
                    context,
                    "Field is empty", Toast.LENGTH_SHORT
                )
                toast.show()
            } else sendMessage(message, userUid, TYPE_TEXT){
                chat_input_message.setText("")
            }
        }

        chat_btn_send_photo.setOnClickListener{
            Log.d("AAA", "bottom was touched")
            sendImage()
        }
    }

    private fun sendImage() {
        Log.d("AAA", "fun has started")
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .start((activity as MainActivity), this)
    }

    private fun initFields (){
        mSwipeRefreshLayout = chat_swipe_refresh
        mRefChat = Firebase.database.reference.child("chats").child(args.currentChat.name)
        refStoreg = FirebaseStorage.getInstance().reference
    }
    private fun initInfoToolBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = args.currentChat.nameRus
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /* Активность которая запускается для получения картинки для фото пользователя */
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null
        ) {
            val imageUri = CropImage.getActivityResult(data).uri
            Log.i("AAA", imageUri.toString())
            val messageKey = mRefChat.child("messages").child(userUid).push().key.toString()
            val path = refStoreg.child(args.currentChat.name).child("ChatImages").child(messageKey)
            putImageToStorage(imageUri, path) {
                getUrlFromStorage(path) {
                    Log.d("AAA", it)
                }
            }
        }
    }
    inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
        /* Функция высшего порядка, отправляет картинку в хранилище */
        path.putFile(uri)
            .addOnSuccessListener { function() }
            .addOnFailureListener {  }

    }
    inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
        /* Функция высшего порядка, получает  URL картинки из хранилища */
        path.downloadUrl
            .addOnSuccessListener { function(it.toString()) }
            .addOnFailureListener {  }
    }

    private fun initRecView(){
        messageRecView = chat_recycle_view
        mAdapter = SingleChatAdapter()
        mRefMesseges = Firebase.database.reference.child("chats/${args.currentChat.name}/messages")
        messageRecView.adapter = mAdapter
        messageListener = AppChildEventListener{
            mAdapter.addItem(it.getMessage(), mSmoothScrollToPosition){
                if (mSmoothScrollToPosition) {
                    messageRecView.smoothScrollToPosition(mAdapter.itemCount)
                }
                mSwipeRefreshLayout.isRefreshing = false
            }
        }

        mRefMesseges.limitToLast(mCountMessages).addChildEventListener(messageListener)

        messageRecView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling&& dy< 0){
                    updateData()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    mIsScrolling= true
                }
            }
        })

        mSwipeRefreshLayout.setOnRefreshListener { updateData() }

    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages+=10
        mRefMesseges.removeEventListener(messageListener)
        mRefMesseges.limitToLast(mCountMessages).addChildEventListener(messageListener)

    }

    private fun sendMessage(message: String, id: String, typeText: String, function: () -> Unit) {
        val messageKey = Firebase.database.reference.child(userUid).push().key
        val mapMessage = hashMapOf<String, Any>()
        mapMessage["from"] = Firebase.auth.currentUser!!.uid.toString()
        mapMessage["fromName"] = "Даниил Арановский"// нужно будет заменить на имя из базы данных
        mapMessage["type"] = typeText
        mapMessage["text"] = message
        mapMessage["timeSTAMP"] = ServerValue.TIMESTAMP

        val mapDialog = hashMapOf<String, Any>()
        mapDialog["chats/${args.currentChat.name}/messages/$messageKey"] = mapMessage
        Firebase.database.reference.updateChildren(mapDialog)
            .addOnSuccessListener { function() }
            .addOnFailureListener{
                val toast = Toast.makeText(
                    context,
                    it.message.toString(),
                    Toast.LENGTH_SHORT)
                toast.show()}
    }

    override fun onResume() {
        super.onResume()

    }
    override fun onPause() {
        super.onPause()
        mRefMesseges.removeEventListener(messageListener)
        mRefMesseges.removeEventListener(messageListener)

    }
    fun DataSnapshot.getMessage(): Message = this.getValue(Message::class.java) ?: Message()
}

