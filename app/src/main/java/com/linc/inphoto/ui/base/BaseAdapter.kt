package com.linc.inphoto.ui.base

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.linc.inphoto.ui.base.state.ItemUiState

@Deprecated(level = DeprecationLevel.HIDDEN, message = "Use Groupie items instead")
abstract class BaseAdapter<T : ItemUiState, H : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<H>() {
    protected val diffCallback = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.getStateItemId() == newItem.getStateItemId()
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.equals(newItem)
        }
    }

    protected val items: AsyncListDiffer<T> = AsyncListDiffer(this, diffCallback)

    override fun getItemCount(): Int = items.currentList.count()

    open fun update(items: List<T>) {
        this.items.submitList(items)
    }
}