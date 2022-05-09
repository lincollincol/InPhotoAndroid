package com.linc.inphoto.data.repository

import com.linc.inphoto.data.mapper.toModel
import com.linc.inphoto.data.network.firebase.ChatsCollection
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.chat.Chat
import kotlinx.coroutines.*
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val chatsCollection: ChatsCollection,
    private val authPreferences: AuthPreferences,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun loadUserChats(): List<Chat> = withContext(ioDispatcher) {
        return@withContext chatsCollection.loadUserChats(authPreferences.userId)
            .map { chat ->
                val receiverId = chat.participants.first { it != authPreferences.userId }
                async { userRepository.getUserById(receiverId)?.let(chat::toModel) }
            }
            .awaitAll()
            .filterNotNull()
    }

    suspend fun loadChat(chatId: String): Chat? = withContext(ioDispatcher) {
        val chat = chatsCollection.loadChat(chatId)
        val receiverId = chat.participants.first { it != authPreferences.userId }
        return@withContext userRepository.getUserById(receiverId)?.let(chat::toModel)
    }

}