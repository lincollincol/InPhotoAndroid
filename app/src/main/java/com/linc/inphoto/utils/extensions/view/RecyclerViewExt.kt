package com.linc.inphoto.utils.extensions.view

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.linc.inphoto.utils.recyclerview.manager.SquareGridLayoutManager

fun RecyclerView.enableItemChangeAnimation(enable: Boolean) {
    (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = enable
}

fun RecyclerView.scrollToStart() = scrollToPosition(0)

fun RecyclerView.scrollToEnd() = scrollToPosition(adapter?.itemCount ?: 0)

fun RecyclerView.smoothScrollToEnd() = smoothScrollToPosition((adapter?.itemCount ?: 0) - 1)

fun RecyclerView.smoothScrollToStart() = smoothScrollToPosition(0)

fun RecyclerView.horizontalLinearLayoutManager(reverseLayout: Boolean = false) =
    linearLayoutManager(LinearLayoutManager.HORIZONTAL, reverseLayout)

fun RecyclerView.verticalLinearLayoutManager(reverseLayout: Boolean = false) =
    linearLayoutManager(LinearLayoutManager.VERTICAL, reverseLayout)

fun RecyclerView.horizontalGridLayoutManager(
    spanCount: Int,
    reverseLayout: Boolean = false
) = gridLayoutManager(spanCount, GridLayoutManager.HORIZONTAL, reverseLayout)

fun RecyclerView.verticalGridLayoutManager(
    spanCount: Int,
    reverseLayout: Boolean = false
) = gridLayoutManager(spanCount, GridLayoutManager.VERTICAL, reverseLayout)

fun RecyclerView.horizontalSquareGridLayoutManager(
    spanCount: Int,
    reverseLayout: Boolean = false
) = squareGridLayoutManager(spanCount, GridLayoutManager.HORIZONTAL, reverseLayout)

fun RecyclerView.verticalSquareGridLayoutManager(
    spanCount: Int,
    reverseLayout: Boolean = false
) = squareGridLayoutManager(spanCount, GridLayoutManager.VERTICAL, reverseLayout)

private fun RecyclerView.linearLayoutManager(
    orientation: Int,
    reverseLayout: Boolean
) = LinearLayoutManager(context, orientation, reverseLayout)

private fun RecyclerView.gridLayoutManager(
    spanCount: Int,
    orientation: Int,
    reverseLayout: Boolean
) = GridLayoutManager(context, spanCount, orientation, reverseLayout)

private fun RecyclerView.squareGridLayoutManager(
    spanCount: Int,
    orientation: Int,
    reverseLayout: Boolean
) = SquareGridLayoutManager(context, spanCount, orientation, reverseLayout)