package com.linc.inphoto.data.repository

import android.net.Uri
import com.linc.inphoto.data.android.MediaLocalDataSource
import com.linc.inphoto.data.mapper.toModel
import com.linc.inphoto.data.network.api.ContentApiService
import com.linc.inphoto.data.network.firebase.MediaStorage
import com.linc.inphoto.data.network.firebase.MessagesCollection
import com.linc.inphoto.data.network.model.chat.AttachmentFirebaseModel
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.utils.extensions.isUrl
import com.linc.inphoto.utils.extensions.mapAsync
import com.linc.inphoto.utils.extensions.toMultipartBody
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val messagesCollection: MessagesCollection,
    private val mediaStorage: MediaStorage,
    private val contentApiService: ContentApiService,
    private val authPreferences: AuthPreferences,
    private val mediaLocalDataSource: MediaLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getChatMessages(chatId: String?): Flow<List<Message>> {
        return messagesCollection.getChatMessages(chatId)
            .map { list ->
                list.map { it.toModel(it.userId != authPreferences.userId) }
            }
    }

    suspend fun loadChatMessages(chatId: String?): List<Message> = withContext(ioDispatcher) {
        return@withContext messagesCollection.loadChatMessages(chatId)
            .map { it.toModel(it.userId != authPreferences.userId) }
    }

    suspend fun sendChatMessage(
        chatId: String?,
        messageId: String,
        message: String,
        attachments: List<Uri>
    ) = withContext(ioDispatcher) {
        val attachmentUrls = attachments
            .mapAsync {
                val file = mediaLocalDataSource.createTempMediaFile(it) ?: return@mapAsync null
                AttachmentFirebaseModel(mediaStorage.uploadFile(file).orEmpty(), file.type)
            }
            .awaitAll()
            .filterNotNull()
        messagesCollection.sendChatMessages(
            chatId,
            messageId,
            authPreferences.userId,
            message,
            // TODO: replace with real attachments
            listOf()
        )
    }

    suspend fun updateChatMessage(
        chatId: String?,
        messageId: String,
        text: String,
        files: List<Uri>
    ) = withContext(ioDispatcher) {
        val filesUrls = files.map { fileUri ->
            async {
                if (fileUri.isUrl()) return@async fileUri.toString()
                mediaLocalDataSource.createTempFile(fileUri)?.let {
                    it.deleteOnExit()
                    contentApiService.uploadChatContent(it.toMultipartBody()).body
                }
            }
        }.awaitAll()
        messagesCollection.updateChatMessages(
            chatId,
            messageId,
            text,
            filesUrls.filterNotNull()
        )
    }

    suspend fun deleteChatMessage(chatId: String?, messageId: String) = withContext(ioDispatcher) {
        if (chatId != null) messagesCollection.deleteChatMessages(chatId, messageId)
    }
}