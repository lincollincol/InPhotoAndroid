package com.linc.inphoto.ui.view.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.IntRange

class ImageEntity(
    layer: Layer,
    private val bitmap: Bitmap,
    @IntRange(from = 1) canvasWidth: Int,
    @IntRange(from = 1) canvasHeight: Int
) : MotionEntity(layer, canvasWidth, canvasHeight) {
    public override fun drawContent(canvas: Canvas, drawingPaint: Paint?) {
        canvas.drawBitmap(bitmap, matrix, drawingPaint)
    }

    override val width: Int
        get() = bitmap.width
    override val height: Int
        get() = bitmap.height

    override fun release() {
        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
    }

    init {
        val width = bitmap.width.toFloat()
        val height = bitmap.height.toFloat()
        val widthAspect = 1.0F * canvasWidth / width
        val heightAspect = 1.0F * canvasHeight / height
        // fit the smallest size
        holyScale = Math.min(widthAspect, heightAspect)

        // initial position of the entity
        srcPoints[0] = 0F
        srcPoints[1] = 0F
        srcPoints[2] = width
        srcPoints[3] = 0F
        srcPoints[4] = width
        srcPoints[5] = height
        srcPoints[6] = 0F
        srcPoints[7] = height
        srcPoints[8] = 0F
        srcPoints[8] = 0F
    }
}