package com.linc.inphoto.utils.view.progress

import com.genius.multiprogressbar.MultiProgressBar

class MultiProgressFinishListener(
    private val onFinished: () -> Unit
) : MultiProgressBar.ProgressFinishListener {
    override fun onProgressFinished() {
        onFinished()
    }
}