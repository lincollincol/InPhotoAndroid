package com.linc.inphoto.ui.view.imageoverlay.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.annotation.IntRange
import com.linc.inphoto.utils.extensions.cast

class TextEntity(
    textLayer: TextLayer,
    @IntRange(from = 1) canvasWidth: Int,
    @IntRange(from = 1) canvasHeight: Int,
//    private val fontProvider: FontProvider
) : MotionEntity(textLayer, canvasWidth, canvasHeight) {
    private val textPaint: TextPaint
    private var bitmap: Bitmap? = null
    private fun updateEntity(moveToPreviousCenter: Boolean) {

        // save previous center
        val oldCenter = absoluteCenter()
        val newBmp = createBitmap(layer, bitmap)

        // recycle previous bitmap (if not reused) as soon as possible
        if (bitmap != null && bitmap != newBmp && !bitmap!!.isRecycled) {
            bitmap!!.recycle()
        }
        bitmap = newBmp
        val width = bitmap!!.width.toFloat()
        val height = bitmap!!.height.toFloat()
        val widthAspect = 1.0f * canvasWidth / width

        // for text we always match text width with parent width
        holyScale = widthAspect

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
        if (moveToPreviousCenter) {
            // move to previous center
            moveCenterTo(oldCenter)
        }
    }

    /**
     * If reuseBmp is not null, and size of the new bitmap matches the size of the reuseBmp,
     * new bitmap won't be created, reuseBmp it will be reused instead
     *
     * @param textLayer text to draw
     * @param reuseBmp  the bitmap that will be reused
     * @return bitmap with the text
     */
    private fun createBitmap(textLayer: TextLayer, reuseBmp: Bitmap?): Bitmap {
        val boundsWidth = canvasWidth

        // init params - size, color, typeface
        textPaint.style = Paint.Style.FILL
//        textPaint.textSize = textLayer.font?.size * canvasWidth
        textPaint.textSize = textLayer.font?.size?.times(canvasWidth) ?: 0F
        textPaint.color = textLayer.font?.color ?: 0
//        textPaint.typeface = fontProvider.getTypeface(textLayer.font?.typeface)

        // drawing text guide : http://ivankocijan.xyz/android-drawing-multiline-text-on-canvas/
        // Static layout which will be drawn on canvas
        val sl = StaticLayout(
            textLayer.text,  // - text which will be drawn
            textPaint,
            boundsWidth,  // - width of the layout
            Layout.Alignment.ALIGN_CENTER,  // - layout alignment
            1F,  // 1 - text spacing multiply
            1F,  // 1 - text spacing add
            true
        ) // true - include padding

        // calculate height for the entity, min - Limits.MIN_BITMAP_HEIGHT
        val boundsHeight = sl.height

        // create bitmap not smaller than TextLayer.Limits.MIN_BITMAP_HEIGHT
        val bmpHeight = (canvasHeight * Math.max(
            TextLayer.Limits.Companion.MIN_BITMAP_HEIGHT,
            1.0f * boundsHeight / canvasHeight
        )).toInt()

        // create bitmap where text will be drawn
        val bmp: Bitmap
        if (reuseBmp != null && reuseBmp.width == boundsWidth && reuseBmp.height == bmpHeight) {
            // if previous bitmap exists, and it's width/height is the same - reuse it
            bmp = reuseBmp
            bmp.eraseColor(Color.TRANSPARENT) // erase color when reusing
        } else {
            bmp = Bitmap.createBitmap(boundsWidth, bmpHeight, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(bmp)
        canvas.save()

        // move text to center if bitmap is bigger that text
        if (boundsHeight < bmpHeight) {
            //calculate Y coordinate - In this case we want to draw the text in the
            //center of the canvas so we move Y coordinate to center.
            val textYCoordinate = ((bmpHeight - boundsHeight) / 2).toFloat()
            canvas.translate(0f, textYCoordinate)
        }

        //draws static layout on canvas
        sl.draw(canvas)
        canvas.restore()
        return bmp
    }


    override val layer: TextLayer
        get() = layer.cast()

    override fun drawContent(canvas: Canvas, drawingPaint: Paint?) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap!!, matrix, drawingPaint)
        }
    }

    override val width: Int
        get() = if (bitmap != null) bitmap!!.width else 0
    override val height: Int
        get() = if (bitmap != null) bitmap!!.height else 0

    fun updateEntity() {
        updateEntity(true)
    }

    override fun release() {
        if (bitmap != null && !bitmap!!.isRecycled) {
            bitmap!!.recycle()
        }
    }

    init {
        textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        updateEntity(false)
    }
}