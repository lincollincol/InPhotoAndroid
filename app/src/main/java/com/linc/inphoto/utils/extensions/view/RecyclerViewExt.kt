package com.linc.inphoto.utils.extensions.view

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.linc.inphoto.utils.recyclerview.manager.SquareGridLayoutManager

fun RecyclerView.enableItemChangeAnimation(enable: Boolean) {
    (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = enable

}

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

fun RecyclerView.horizontalGridLayoutManager(spanCount: Int) =
    gridLayoutManager(spanCount, GridLayoutManager.HORIZONTAL)

fun RecyclerView.verticalGridLayoutManager(
    spanCount: Int
) = gridLayoutManager(spanCount, GridLayoutManager.VERTICAL)

fun RecyclerView.horizontalSquareGridLayoutManager(spanCount: Int) =
    squareGridLayoutManager(spanCount, GridLayoutManager.HORIZONTAL)

fun RecyclerView.verticalSquareGridLayoutManager(
    spanCount: Int
) = squareGridLayoutManager(spanCount, GridLayoutManager.VERTICAL)


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

private fun RecyclerView.squareGridLayoutManager(
    spanCount: Int,
    orientation: Int
) = SquareGridLayoutManager(
    context, spanCount, orientation, false
)