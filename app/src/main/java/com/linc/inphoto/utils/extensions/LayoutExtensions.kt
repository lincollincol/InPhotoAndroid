package com.linc.inphoto.utils.extensions

import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.verticalGridLayoutManager(spanCount: Int) =
    gridLayoutManager(spanCount, GridLayoutManager.VERTICAL)

fun RecyclerView.horizontalGridLayoutManager(spanCount: Int) =
    gridLayoutManager(spanCount, GridLayoutManager.HORIZONTAL)

private fun RecyclerView.gridLayoutManager(
    spanCount: Int,
    orientation: Int
) = GridLayoutManager(
    context, spanCount, orientation, false
)