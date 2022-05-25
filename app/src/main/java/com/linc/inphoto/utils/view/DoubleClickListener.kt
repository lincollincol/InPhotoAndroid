package com.linc.inphoto.utils.view

import android.view.View

class DoubleClickListener(
    private val interval: Long,
    private val onDoubleClick: (view: View) -> Unit,
    private val onSingleClick: ((view: View) -> Unit)?
) : View.OnClickListener {

    private var clicks = 0
    private var isBusy = false

    override fun onClick(view: View) {
        if (!isBusy) {
            //  Prevent multiple click in this short time
            isBusy = true

            // Increase clicks count
            clicks++
            view.postDelayed(Runnable {
                if (clicks >= 2) {  // Double tap.
                    onDoubleClick(view)
                }
                if (clicks == 1) {  // Single tap
                    onSingleClick?.invoke(view)
                }

                // we need to  restore clicks count
                clicks = 0
            }, interval)
            isBusy = false
        }
    }

}