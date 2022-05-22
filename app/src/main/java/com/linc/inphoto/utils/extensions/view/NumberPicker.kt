package com.linc.inphoto.utils.extensions.view

import android.widget.NumberPicker

fun NumberPicker.setValues(values: List<String>) {
    minValue = 0
    maxValue = values.count() - 1
    displayedValues = values.toTypedArray()
}