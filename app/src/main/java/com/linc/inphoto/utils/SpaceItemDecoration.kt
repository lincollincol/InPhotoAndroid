package com.linc.inphoto.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpaceItemDecoration : ItemDecoration {

    private var space: Array<Int>
    private var lastItemBottomSpace = 0

    companion object {
        private const val TOP = 0
        private const val BOTTOM = 1
        private const val START = 2
        private const val END = 3
    }

    constructor(space: Int, bottomSpace: Int) {
        this.space = arrayOf(space, space, space, space)
        this.lastItemBottomSpace = bottomSpace
    }

    constructor(space: Int) : this(space, 0)

    constructor(verticalSpace: Int, horizontalSpace: Int, bottomSpace: Int) {
        this.space = arrayOf(
            verticalSpace,
            verticalSpace,
            horizontalSpace,
            horizontalSpace
        )
        this.lastItemBottomSpace = bottomSpace
    }

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        outRect.left = space[START]
        outRect.right = space[END]
        outRect.bottom = space[BOTTOM]
        outRect.top = space[TOP]
        if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.bottom = lastItemBottomSpace
        }
    }
}