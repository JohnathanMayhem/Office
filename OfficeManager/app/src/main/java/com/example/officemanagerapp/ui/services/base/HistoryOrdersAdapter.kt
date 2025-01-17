package com.example.officemanagerapp.ui.services.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.officemanagerapp.R
import com.example.officemanagerapp.databinding.ItemOrderBinding
import com.example.officemanagerapp.models.Order

class HistoryOrdersAdapter(val callback: OrderClick, val reviewClicked: ReviewClick) : RecyclerView.Adapter<HistoryOrdersAdapter.HistoryPlannedOrdersViewHolder>() {

    /**
     * The NewsItem that our Adapter will show
     */
    var historyOrders: List<Order> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryPlannedOrdersViewHolder {
        val withDataBinding: ItemOrderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            HistoryPlannedOrdersViewHolder.LAYOUT,
            parent,
            false)
        return HistoryPlannedOrdersViewHolder(withDataBinding)
    }

    override fun getItemCount() = historyOrders.size

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: HistoryPlannedOrdersViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.order = historyOrders[position]
            it.orderClick = callback
            it.reviewClick = reviewClicked
        }
    }

    /**
     * ViewHolder for news items. All work is done by data binding.
     */
    class HistoryPlannedOrdersViewHolder(val viewDataBinding: ItemOrderBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_order
        }
    }
}