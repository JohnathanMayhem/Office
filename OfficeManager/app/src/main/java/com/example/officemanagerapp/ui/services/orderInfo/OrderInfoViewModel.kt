package com.example.officemanagerapp.ui.services.orderInfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.officemanagerapp.models.Photo

class OrderInfoViewModel (
    private val state: SavedStateHandle
) : ViewModel() {

    var comment: String = ""

    private val _photos = MutableLiveData<List<Photo>>(listOf())
    val photos: MutableLiveData<List<Photo>>
        get() = _photos
}

