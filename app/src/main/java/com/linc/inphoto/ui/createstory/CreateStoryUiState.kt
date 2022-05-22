package com.linc.inphoto.ui.createstory

import android.net.Uri
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.utils.extensions.ONE_HOUR_IN_MILLIS
import com.linc.inphoto.utils.extensions.TEN_SECONDS_IN_MILLIS

data class CreateStoryUiState(
    val contentUri: Uri? = null,
    val durationMillis: Long = TEN_SECONDS_IN_MILLIS,
    val expirationTimeMillis: Long = ONE_HOUR_IN_MILLIS
) : UiState