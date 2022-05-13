package com.linc.inphoto.data.repository

import com.linc.inphoto.data.mapper.toModel
import com.linc.inphoto.data.network.firebase.ChatsCollection
import com.linc.inphoto.data.network.firebase.MessagesCollection
import com.linc.inphoto.data.network.model.chat.ChatFirebaseModel
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.chat.Chat
import kotlinx.coroutines.*
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val chatsCollection: ChatsCollection,
    private val messagesCollection: MessagesCollection,
    private val authPreferences: AuthPreferences,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun loadUserChats(): List<Chat> = withContext(ioDispatcher) {
        return@withContext chatsCollection.loadUserChats(authPreferences.userId)
            .map { loadChatDetails(it) }
    }

    suspend fun loadChat(chatId: String): Chat = withContext(ioDispatcher) {
        return@withContext loadChatDetails(chatsCollection.loadChat(chatId))
    }

    private suspend fun loadChatDetails(chat: ChatFirebaseModel): Chat = withContext(ioDispatcher) {
        val receiverId = chat.participants.first { it != authPreferences.userId }
        val message = async { messagesCollection.loadLastChatMessage(chat.id) }
        val user = async { userRepository.getUserById(receiverId) }
        return@withContext chat.toModel(user.await(), message.await())
    }

}