package com.linc.inphoto.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.entity.AspectRatio
import com.linc.inphoto.utils.extensions.getColorInt
import com.linc.inphoto.utils.extensions.toPx

class AspectRatioPreviewView : View {
    private var colorRectNotSelected = 0
    private var colorRectSelected = 0
    var ratio: AspectRatio? = null
        private set
    private var isSelected = false
    private var rectPaint: Paint? = null
    private var centerX = 0f
    private var previewRect: RectF? = null

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

    fun setAspectRatio(w: Float, h: Float) {
        setAspectRatio(AspectRatio(w, h))
    }

    fun setAspectRatio(ratio: AspectRatio?) {
        this.ratio = ratio
        if (width != 0 && height != 0) {
            configurePreviewRect()
        }
        invalidate()
    }

    private fun configurePreviewRect() {
        val freeSpace = RectF(0f, 0f, width.toFloat(), height.toFloat())
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

    init {
        colorRectSelected = context.getColorInt(R.color.dream_setting)
        colorRectNotSelected = context.getColorInt(R.color.black)
        rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.toPx
        }
        colorRectNotSelected.also { rectPaint?.color = it }
    }
}
