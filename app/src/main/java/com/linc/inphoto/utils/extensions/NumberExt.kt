package com.linc.inphoto.utils.extensions

import android.content.res.Resources
import android.util.TypedValue

val Number.toPx get() = applyNumberDimension(this, TypedValue.COMPLEX_UNIT_DIP)

val Number.toSp get() = applyNumberDimension(this, TypedValue.COMPLEX_UNIT_SP)

private fun applyNumberDimension(
    number: Number,
    unit: Int
) = TypedValue.applyDimension(
    unit,
    number.toFloat(),
    Resources.getSystem().displayMetrics
)