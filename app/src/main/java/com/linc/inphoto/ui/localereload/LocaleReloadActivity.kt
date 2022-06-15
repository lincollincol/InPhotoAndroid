package com.linc.inphoto.ui.localereload

import android.app.Activity
import android.os.Bundle
import com.linc.inphoto.R
import com.linc.inphoto.utils.extensions.postDelayed


class LocaleReloadActivity : Activity() {

    companion object {
        private const val FINISH_DELAY = 500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        setContentView(R.layout.activity_locale_reload)
        postDelayed(FINISH_DELAY, ::finish)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}