package com.linc.inphoto.ui.view.model

import androidx.annotation.FloatRange

open class Layer {
    /**
     * rotation relative to the layer center, in degrees
     */
    @FloatRange(from = 0.0, to = 360.0)
    var rotationInDegrees = 0f
    var scale = 0f

    /**
     * top left X coordinate, relative to parent canvas
     */
    var x = 0f

    /**
     * top left Y coordinate, relative to parent canvas
     */
    var y = 0f

    /**
     * is layer flipped horizontally (by X-coordinate)
     */
    var isFlipped = false
    protected open fun reset() {
        rotationInDegrees = 0.0f
        scale = 1.0f
        isFlipped = false
        x = 0.0f
        y = 0.0f
    }

    fun postScale(scaleDiff: Float) {
        val newVal = scale + scaleDiff
        if (newVal >= Limits.MIN_SCALE && newVal <= Limits.MAX_SCALE) {
            scale = newVal
        }
    }

    fun postRotate(rotationInDegreesDiff: Float) {
        rotationInDegrees += rotationInDegreesDiff
        rotationInDegrees %= 360.0f
    }

    fun postTranslate(dx: Float, dy: Float) {
        x += dx
        y += dy
    }

    fun flip() {
        isFlipped = !isFlipped
    }

    open fun initialScale(): Float {
        return Limits.INITIAL_ENTITY_SCALE
    }

    internal interface Limits {
        companion object {
            const val MIN_SCALE = 0.06f
            const val MAX_SCALE = 4.0f
            const val INITIAL_ENTITY_SCALE = 0.4f
        }
    }

    init {
        reset()
    }
}