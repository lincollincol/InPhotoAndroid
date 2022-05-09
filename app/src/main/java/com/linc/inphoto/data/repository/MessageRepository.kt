package com.linc.inphoto.data.repository

import com.linc.inphoto.data.mapper.toModel
import com.linc.inphoto.data.network.firebase.MessagesCollection
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.chat.Message
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val messagesCollection: MessagesCollection,
    private val authPreferences: AuthPreferences,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun loadChatMessages(chatId: String?): List<Message> = withContext(ioDispatcher) {
        chatId ?: return@withContext emptyList()
        return@withContext messagesCollection.loadChatMessages(chatId)
            .map { it.toModel(it.userId != authPreferences.userId) }
    }

}