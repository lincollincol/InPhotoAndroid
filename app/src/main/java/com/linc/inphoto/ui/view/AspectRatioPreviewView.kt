package com.linc.inphoto.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.utils.extensions.toPx
import com.linc.inphoto.utils.extensions.toSp
import com.steelkiwi.cropiwa.AspectRatio
import com.steelkiwi.cropiwa.util.ResUtil
import java.util.*

class AspectRatioPreviewView : View {
    private var colorRectNotSelected = 0
    private var colorRectSelected = 0
    private var colorText = 0
    var ratio: AspectRatio? = null
        private set
    private var isSelected = false
    private var rectPaint: Paint? = null
    private var textPaint: Paint? = null
    private var centerX = 0f
    private var previewRect: RectF? = null
    private var textOutBounds: Rect? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w * 0.5f
        if (ratio != null) {
            configurePreviewRect()
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (previewRect != null) {
            canvas.drawRect(previewRect!!, rectPaint!!)
            val text = aspectRatioString
            textPaint!!.getTextBounds(text, 0, text.length, textOutBounds)
            canvas.drawText(
                text, centerX - textOutBounds!!.width() * 0.5f,
                bottom - textOutBounds!!.height() * 0.5f,
                textPaint!!
            )
        }
    }

    override fun setSelected(selected: Boolean) {
        isSelected = selected
        rectPaint!!.color = if (selected) colorRectSelected else colorRectNotSelected
        invalidate()
    }

    override fun isSelected(): Boolean {
        return isSelected
    }

    fun setAspectRatio(w: Int, h: Int) {
        this.ratio = AspectRatio(w, h)
        if (width != 0 && height != 0) {
            configurePreviewRect()
        }
        invalidate()
    }

    fun setAspectRatio(ratio: AspectRatio?) {
        this.ratio = ratio
        if (width != 0 && height != 0) {
            configurePreviewRect()
        }
        invalidate()
    }

    private fun configurePreviewRect() {
        val str = aspectRatioString
        textPaint!!.getTextBounds(str, 0, str.length, textOutBounds)
        val freeSpace = RectF(0f, 0f, width.toFloat(), height - textOutBounds!!.height() * 1.2f)
        val calculateFromWidth = (ratio!!.height < ratio!!.width
                || ratio!!.isSquare && freeSpace.width() < freeSpace.height())
        val halfWidth: Float
        val halfHeight: Float
        if (calculateFromWidth) {
            halfWidth = freeSpace.width() * 0.8f * 0.5f
            halfHeight = halfWidth / ratio!!.ratio
        } else {
            halfHeight = freeSpace.height() * 0.8f * 0.5f
            halfWidth = halfHeight * ratio!!.ratio
        }
        previewRect = RectF(
            freeSpace.centerX() - halfWidth, freeSpace.centerY() - halfHeight,
            freeSpace.centerX() + halfWidth, freeSpace.centerY() + halfHeight
        )
    }

    private val aspectRatioString: String
        get() = if (ratio == null) {
            ""
        } else {
            String.format(Locale.US, "%d:%d", ratio!!.width, ratio!!.height)
        }

    companion object {
        private const val SIZE_TEXT = 12
    }

    init {
        textOutBounds = Rect()
        val r = ResUtil(context)
        colorRectSelected = r.color(R.color.dream_setting)
        colorRectNotSelected = r.color(R.color.black)
        colorText = r.color(R.color.black)
        rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.toPx
        }
        colorRectNotSelected.also { rectPaint?.color = it }
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = SIZE_TEXT.toSp
            color = colorText
        }
    }
}
