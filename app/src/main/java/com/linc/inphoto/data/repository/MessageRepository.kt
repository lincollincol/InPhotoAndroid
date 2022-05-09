package com.linc.inphoto.data.repository

import com.linc.inphoto.data.network.firebase.MessagesCollection
import com.linc.inphoto.entity.chat.Message
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val messagesCollection: MessagesCollection,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun loadLastChatMessage(chatId: String): Message? = withContext(ioDispatcher) {
        messagesCollection.loadLastChatMessage(chatId)
        return@withContext null
    }

}