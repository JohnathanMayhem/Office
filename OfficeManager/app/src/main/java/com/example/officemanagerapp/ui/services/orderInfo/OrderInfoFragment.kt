package com.example.officemanagerapp.ui.services.orderInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.officemanagerapp.R
import com.example.officemanagerapp.databinding.FragmentOrderInfoBinding
import com.example.officemanagerapp.models.Photo
import com.github.dhaval2404.imagepicker.ImagePicker

class PlannedInfoFragment : Fragment() {

    private var photoAdapter: PhotoAdapter? = null
    private val infoViewModel: OrderInfoViewModel by viewModels()
    private val args by navArgs<PlannedInfoFragmentArgs>()

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
        if (intent.data?.data != null) {
            // TODO(Add the restriction to number of photos that user can upload)
            val oldList = infoViewModel.photos.value?.toMutableList()
            oldList?.add(Photo(uri = intent.data?.data))
            infoViewModel.photos.value = oldList?.toList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentOrderInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_info, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        photoAdapter = PhotoAdapter(PhotoRemoveClick {
            val oldList = infoViewModel.photos.value?.toMutableList()
            oldList?.remove(it)
            infoViewModel.photos.value = oldList?.toList()
        })

        binding.root.findViewById<RecyclerView>(R.id.photos_rv).apply {
            layoutManager = GridLayoutManager(activity, 3)
            adapter = photoAdapter
            setHasFixedSize(true)
        }

        binding.btnAddPhoto.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent {
                    getContent.launch(it)
                }
        }

        binding.editTextOrder.addTextChangedListener {
            infoViewModel.comment = it.toString()
        }

        binding.btnNext.setOnClickListener {

            if (args.plannedOrderPost != null) {
                var orderPost = args.plannedOrderPost
                orderPost = orderPost!!.copy(comment = infoViewModel.comment)
                val action = PlannedInfoFragmentDirections.actionPlannedInfoFragmentToPlannedDateFragment(args.appBarTitle, plannedOrderPost = orderPost)
                findNavController().navigate(action)
            } else if (args.marketOrderPost != null) {
                var orderPost = args.marketOrderPost
                orderPost = orderPost!!.copy(comment = infoViewModel.comment)
                val action = PlannedInfoFragmentDirections.actionPlannedInfoFragmentToPlannedDateFragment(args.appBarTitle, marketOrderPost = orderPost)
                findNavController().navigate(action)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        infoViewModel.photos.observe(viewLifecycleOwner) {
            photoAdapter?.photos = infoViewModel.photos.value!!.toList()
        }
    }
}

class PhotoRemoveClick(val block: (Photo) -> Unit) {
    fun onClick(photo: Photo) = block(photo)
}
