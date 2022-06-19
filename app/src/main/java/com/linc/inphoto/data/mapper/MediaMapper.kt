package com.linc.inphoto.data.mapper

import com.linc.inphoto.data.network.model.media.RemoteMediaApiModel
import com.linc.inphoto.entity.media.RemoteMedia

fun RemoteMediaApiModel.toModel() = RemoteMedia(
    url = url,
    name = name,
    mimeType = mimeType
)