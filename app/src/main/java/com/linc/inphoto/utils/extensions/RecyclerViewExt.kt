package com.linc.inphoto.utils.extensions

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.scrollToStart() {
    scrollToPosition(0)
}

fun RecyclerView.smoothScrollToStart() {
    smoothScrollToPosition(0)
}

fun RecyclerView.horizontalLinearLayoutManager() =
    linearLayoutManager(LinearLayoutManager.HORIZONTAL)

fun RecyclerView.verticalLinearLayoutManager() =
    linearLayoutManager(LinearLayoutManager.VERTICAL)

fun RecyclerView.verticalGridLayoutManager(spanCount: Int) =
    gridLayoutManager(spanCount, GridLayoutManager.VERTICAL)

fun RecyclerView.horizontalGridLayoutManager(spanCount: Int) =
    gridLayoutManager(spanCount, GridLayoutManager.HORIZONTAL)

private fun RecyclerView.linearLayoutManager(
    orientation: Int
) = LinearLayoutManager(
    context, orientation, false
)

private fun RecyclerView.gridLayoutManager(
    spanCount: Int,
    orientation: Int
) = GridLayoutManager(
    context, spanCount, orientation, false
)