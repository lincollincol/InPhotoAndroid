package com.linc.inphoto.utils

import android.text.InputFilter
import android.text.Spanned

class TextInputFilter(
    private val onFilter: (
        text: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ) -> CharSequence?
) : InputFilter {
    override fun filter(
        text: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        return onFilter(text, start, end, dest, dstart, dend)
    }
}