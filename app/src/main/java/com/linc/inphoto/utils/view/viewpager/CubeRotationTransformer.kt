package com.linc.inphoto.utils.view.viewpager

import android.view.View
import androidx.viewpager2.widget.ViewPager2


class CubeRotationTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        val deltaY = 0.5F

        view.pivotX = if (position < 0F) view.width.toFloat() else 0F
        view.pivotY = view.height * deltaY
        view.rotationY = 45F * position
    }
}