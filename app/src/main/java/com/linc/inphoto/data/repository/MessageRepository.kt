package com.linc.inphoto.data.repository

import android.net.Uri
import com.linc.inphoto.data.android.MediaLocalDataSource
import com.linc.inphoto.data.mapper.toModel
import com.linc.inphoto.data.network.api.ContentApiService
import com.linc.inphoto.data.network.firebase.MessagesCollection
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.utils.extensions.toMultipartBody
import kotlinx.coroutines.*
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val messagesCollection: MessagesCollection,
    private val contentApiService: ContentApiService,
    private val authPreferences: AuthPreferences,
    private val mediaLocalDataSource: MediaLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun loadChatMessages(chatId: String?): List<Message> = withContext(ioDispatcher) {
        chatId ?: return@withContext emptyList()
        return@withContext messagesCollection.loadChatMessages(chatId)
            .map { it.toModel(it.userId != authPreferences.userId) }
    }

    suspend fun sendChatMessage(
        chatId: String?,
        message: String,
        files: List<Uri>
    ): Message? = withContext(ioDispatcher) {
        chatId ?: return@withContext null
        val filesUrls = files.map { fileUri ->
            async {
                mediaLocalDataSource.createTempFile(fileUri)?.let {
                    it.deleteOnExit()
                    contentApiService.uploadChatContent(it.toMultipartBody()).body
                }
            }
        }.awaitAll()
        return@withContext messagesCollection.sendChatMessages(
            chatId,
            authPreferences.userId,
            message,
            filesUrls.filterNotNull()
        ).toModel(isIncoming = false)
    }

    suspend fun deleteChatMessage(chatId: String?, messageId: String) = withContext(ioDispatcher) {
        if (chatId != null) messagesCollection.deleteChatMessages(chatId, messageId)
    }
}