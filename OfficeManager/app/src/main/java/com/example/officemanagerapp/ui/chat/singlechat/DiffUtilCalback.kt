package com.example.officemanagerapp.ui.chat.singlechat

import androidx.recyclerview.widget.DiffUtil
import com.example.officemanagerapp.ui.chat.singlechat.Message

class DiffUtilCalback (
    private val oldList: List<Message>,
    private val newList: List<Message>
    ):DiffUtil.Callback(){
    override fun getOldListSize(): Int =oldList.size

    override fun getNewListSize(): Int =newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].timeSTAMP == newList[newItemPosition].timeSTAMP

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition]==newList[newItemPosition]

}