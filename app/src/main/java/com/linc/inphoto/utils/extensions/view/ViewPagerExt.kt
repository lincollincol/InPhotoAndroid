package com.linc.inphoto.utils.extensions.view

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.linc.inphoto.utils.extensions.cast
import com.linc.inphoto.utils.extensions.safeCast

/**
 * Reduces drag sensitivity of [ViewPager2] widget
 */
fun ViewPager2.reduceDragSensitivity() {
    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
    recyclerViewField.isAccessible = true
    val recyclerView = recyclerViewField.get(this).safeCast<RecyclerView>()
    val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
    touchSlopField.isAccessible = true
    val touchSlop = touchSlopField.get(recyclerView).cast<Int>()
    touchSlopField.set(recyclerView, touchSlop * 8)
}