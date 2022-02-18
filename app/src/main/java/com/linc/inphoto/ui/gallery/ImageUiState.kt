package com.linc.inphoto.ui.gallery

import android.net.Uri
import com.linc.inphoto.entity.LocalMedia

data class ImageUiState(
    val uri: Uri,
    val onClick: () -> Unit
)

fun LocalMedia.toUiState(onClick: () -> Unit) = ImageUiState(uri, onClick)