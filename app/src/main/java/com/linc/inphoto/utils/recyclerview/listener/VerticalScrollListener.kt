package com.linc.inphoto.utils.recyclerview.listener

import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView

class VerticalScrollListener(
    private val onScrolled: (Int) -> Unit,
) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val gravity = when {
            dy > 0 -> Gravity.BOTTOM
            dy < 0 -> Gravity.TOP
            else -> return
        }
        onScrolled.invoke(gravity)
    }
}