package com.linc.inphoto.utils.extensions

import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupieAdapter

fun GroupieAdapter.setSingleItemSelectionListener(
    recyclerView: RecyclerView,
) {
    var previousItemPosition = 0
    setOnItemClickListener { item, view ->
        val selectedPosition = getAdapterPosition(item)
        item.id
        if (selectedPosition != previousItemPosition) {
            recyclerView.layoutManager?.findViewByPosition(previousItemPosition)?.isSelected = false
            view.isSelected = true
            previousItemPosition = getAdapterPosition(item)
        }
    }
}