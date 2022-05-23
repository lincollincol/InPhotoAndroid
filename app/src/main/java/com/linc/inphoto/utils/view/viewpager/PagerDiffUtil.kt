package com.linc.inphoto.utils.view.viewpager

import androidx.recyclerview.widget.DiffUtil
import com.linc.inphoto.ui.base.state.ItemUiState

class PagerDiffUtil(
    private val oldList: List<ItemUiState>,
    private val newList: List<ItemUiState>
) : DiffUtil.Callback() {

    enum class PayloadKey {
        VALUE
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].getStateItemId() == newList[newItemPosition].getStateItemId()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
        return listOf(PayloadKey.VALUE)
    }
}