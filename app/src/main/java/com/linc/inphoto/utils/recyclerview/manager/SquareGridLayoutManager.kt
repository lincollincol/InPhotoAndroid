package com.linc.inphoto.utils.recyclerview.manager

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SquareGridLayoutManager(
    context: Context,
    spanCount: Int,
    orientation: Int,
    reverseLayout: Boolean,
) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {
    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
        lp?.height = width / spanCount

        return true
    }
}