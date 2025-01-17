package com.example.officemanagerapp.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.officemanagerapp.R

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.item_chat.view.*


class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var mResView: RecyclerView
    private lateinit var mResViwAdapter: FirebaseRecyclerAdapter<Chat, ChatsHolder>
    private lateinit var mRefChats: DatabaseReference
    private lateinit var mRefChatsListener:AppValueEventListener


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
    }


    private fun initRecycleView() {

        mResView = chats_res_view
        mRefChats = Firebase.database.reference.child("chats")//должен быть путь к чатам
        Log.d("Host", mRefChats.toString())
        val options =
            FirebaseRecyclerOptions.Builder<Chat>().setQuery(mRefChats, Chat::class.java).build()
        mResViwAdapter = object : FirebaseRecyclerAdapter<Chat, ChatsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsHolder {
                val view = LayoutInflater.from(parent.context).inflate((R.layout.item_chat), parent, false)
                return ChatsHolder(view)
            }
            override fun onBindViewHolder(holder: ChatsHolder, position: Int, model: Chat) {
                mRefChats =Firebase.database.reference.child("chats").child(model.name)
                mRefChatsListener = AppValueEventListener {
                    val chat = it.getChat()
                    holder.name.text = chat.nameRus
                    holder.itemView.setOnClickListener{
                        val action = ChatFragmentDirections.actionChatFragmentToSingleChatFragment(chat)//не понимаю как заменить один фрагмент другим передав в него параметр
                        findNavController().navigate(action)
                    }
                }

                mRefChats.addValueEventListener(mRefChatsListener)
            }
        }
        mResView.adapter = mResViwAdapter
        Log.d("Adapter", mResView.adapter.toString())
        mResViwAdapter.startListening()
        Log.d("AAA", "we inited")
    }

    class ChatsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.chat_name
    }

    override fun onPause() {
        super.onPause()
        mResViwAdapter.stopListening()
    }

    //
    class AppValueEventListener (val onSuccess:(DataSnapshot) -> Unit) :ValueEventListener{
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(p0: DataSnapshot) {
            onSuccess(p0)
        }

    }
    //

}
fun DataSnapshot.getChat(): Chat = this.getValue(Chat::class.java) ?: Chat()



