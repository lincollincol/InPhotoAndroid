package com.linc.inphoto.data.repository

import com.linc.inphoto.data.android.MediaLocalDataSource
import com.linc.inphoto.data.mapper.toModel
import com.linc.inphoto.data.network.api.ContentApiService
import com.linc.inphoto.data.network.datasource.MediaRemoteDateSource
import com.linc.inphoto.data.network.firebase.MessagesCollection
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.entity.media.LocalMedia
import com.linc.inphoto.utils.extensions.mapAsync
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val messagesCollection: MessagesCollection,
    private val mediaRemoteDateSource: MediaRemoteDateSource,
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
        localAttachments: List<LocalMedia>
    ) = withContext(ioDispatcher) {
        val uploadedAttachments = localAttachments
            .mapAsync(mediaRemoteDateSource::uploadFile)
            .awaitAll()
            .filterNotNull()
        messagesCollection.sendChatMessages(
            chatId,
            messageId,
            authPreferences.userId,
            message,
            uploadedAttachments
        )
    }

    suspend fun updateChatMessage(
        chatId: String?,
        messageId: String,
        text: String
    ) = withContext(ioDispatcher) {
        messagesCollection.updateChatMessages(
            chatId,
            messageId,
            text
        )
    }

    suspend fun deleteChatMessage(chatId: String?, messageId: String) = withContext(ioDispatcher) {
        if (chatId != null) messagesCollection.deleteChatMessages(chatId, messageId)
    }
}