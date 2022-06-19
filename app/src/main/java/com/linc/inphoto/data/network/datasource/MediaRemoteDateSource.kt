package com.linc.inphoto.data.network.datasource

import com.google.firebase.storage.StorageReference
import com.linc.inphoto.data.network.model.media.RemoteMediaApiModel
import com.linc.inphoto.entity.media.LocalMedia
import com.linc.inphoto.utils.extensions.getMimeTypePrefix
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class MediaRemoteDateSource @Inject constructor(
    private val reference: StorageReference,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun uploadFile(
        localMedia: LocalMedia
    ): RemoteMediaApiModel? = withContext(ioDispatcher) {
        val folder = localMedia.mimeType.getMimeTypePrefix()
        val path = "${folder}/${UUID.randomUUID()}"
        reference.child(path)
            .putFile(localMedia.uri)
            .await()
        val mediaFileUrl = getFileUrl(path) ?: return@withContext null
        return@withContext RemoteMediaApiModel(mediaFileUrl, localMedia.name, localMedia.mimeType)
    }

    suspend fun getFileUrl(path: String): String? = withContext(ioDispatcher) {
        return@withContext reference.child(path).downloadUrl.await()?.toString()
    }

}