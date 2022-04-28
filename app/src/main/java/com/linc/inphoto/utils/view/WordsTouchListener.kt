package com.linc.inphoto.utils.view

import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.linc.inphoto.utils.extensions.containsOneOf
import com.linc.inphoto.utils.extensions.extractWord
import com.linc.inphoto.utils.extensions.safeCast

class WordsTouchListener(
    private val text: CharSequence,
    private val words: List<CharSequence>,
    private val onTargetClicked: (CharSequence) -> Unit,
    private val onTextClicked: () -> Unit
) : View.OnTouchListener {
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val offset = view.safeCast<TextView>()?.getOffsetForPosition(event.x, event.y) ?: 0
            val target = text.extractWord(offset, ' ', '\n', '\t')
            if (target.containsOneOf(*words.toTypedArray())) {
                onTargetClicked(target)
            } else {
                onTextClicked()
            }
        }
        return true
    }
}