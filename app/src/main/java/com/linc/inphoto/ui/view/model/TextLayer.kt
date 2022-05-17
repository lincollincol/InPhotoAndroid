package com.linc.inphoto.ui.view.model

class TextLayer : Layer() {
    var text: String? = null
    var font: Font? = null
    override fun reset() {
        super.reset()
        text = ""
        font = Font()
    }

    override fun initialScale(): Float {
        return Limits.INITIAL_SCALE
    }

    interface Limits {
        companion object {
            /**
             * limit text size to view bounds
             * so that users don't put small font size and scale it 100+ times
             */
            const val MAX_SCALE = 1.0F
            const val MIN_SCALE = 0.2F
            const val MIN_BITMAP_HEIGHT = 0.13F
            const val FONT_SIZE_STEP = 0.008F
            const val INITIAL_FONT_SIZE = 0.075F
            const val INITIAL_FONT_COLOR = -0x1000000
            const val INITIAL_SCALE = 0.8F // set the same to avoid text scaling
        }
    }
}