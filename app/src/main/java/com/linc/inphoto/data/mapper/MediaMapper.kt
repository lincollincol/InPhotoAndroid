package com.linc.inphoto.data.mapper

import android.net.Uri
import com.linc.inphoto.data.network.model.media.RemoteMediaApiModel
import com.linc.inphoto.entity.media.RemoteMedia

fun RemoteMediaApiModel.toModel() = RemoteMedia(
    uri = Uri.parse(url),
    name = name,
    mimeType = mimeType,
    extension = extension
)