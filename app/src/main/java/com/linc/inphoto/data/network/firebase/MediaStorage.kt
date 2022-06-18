package com.linc.inphoto.data.network.firebase

import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.linc.inphoto.data.android.model.MediaFile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class MediaStorage @Inject constructor(
    private val reference: StorageReference,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun uploadFile(mediaFile: MediaFile): String? = withContext(ioDispatcher) {
        return@withContext uploadFile(mediaFile.type, mediaFile.uri, mediaFile.source.extension)
    }

    suspend fun uploadFile(
        folder: String,
        file: Uri,
        extension: String
    ): String? = withContext(ioDispatcher) {
        val path = "${folder}/${UUID.randomUUID()}.$extension"
        reference.child(path)
            .putFile(file)
            .await()
        return@withContext getFile(path)
    }

//    suspend fun getFile(folder: String, name: String): String? = withContext(ioDispatcher) {
//        return@withContext getFile("${folder}/$name")
//    }

    suspend fun getFile(path: String): String? = withContext(ioDispatcher) {
        return@withContext reference.child(path).downloadUrl.await()?.toString()
    }

}