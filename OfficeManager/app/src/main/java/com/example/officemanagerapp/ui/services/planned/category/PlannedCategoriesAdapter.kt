package com.example.officemanagerapp.ui.services.planned.category

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.officemanagerapp.R
import com.example.officemanagerapp.databinding.ItemCategoryCardBinding
import com.example.officemanagerapp.models.CategoriesList


class PlannedCategoriesAdapter(val callback: PlannedCategoriesClick) : RecyclerView.Adapter<PlannedCategoriesAdapter.CategoriesViewHolder>() {

    var categoriesLists: List<CategoriesList> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val withDataBinding: ItemCategoryCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CategoriesViewHolder.LAYOUT,
            parent,
            false)
        return CategoriesViewHolder(withDataBinding)
    }

    override fun getItemCount() = categoriesLists.size

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.category = categoriesLists[position]
            it.click = callback
        }
    }

    class CategoriesViewHolder(val viewDataBinding: ItemCategoryCardBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_category_card
        }
    }
}
