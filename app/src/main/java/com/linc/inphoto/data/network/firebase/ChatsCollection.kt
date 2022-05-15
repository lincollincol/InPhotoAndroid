package com.linc.inphoto.data.network.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.linc.inphoto.data.network.model.chat.ChatFirebaseModel
import com.linc.inphoto.utils.getList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class ChatsCollection @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        private const val CHATS_COLLECTION = "chats"
    }

    suspend fun createChat(
        participants: List<String?>
    ): String = withContext(ioDispatcher) {
        if (participants.filterNotNull().count() <= 1) {
            error("Cannot create chat! User not found!")
        }
        val chatId = UUID.randomUUID().toString()
        firestore.collection(CHATS_COLLECTION)
            .document(chatId)
            .set(ChatFirebaseModel(participants.filterNotNull()))
            .await()
        return@withContext chatId
    }

    suspend fun loadUserChats(userId: String): List<ChatFirebaseModel> = withContext(ioDispatcher) {
        return@withContext firestore.collection(CHATS_COLLECTION)
            .whereArrayContains("participants", userId)
            .get()
            .await()
            .map(::getChatFirebaseModel)
    }

    suspend fun loadChat(chatId: String): ChatFirebaseModel = withContext(ioDispatcher) {
        return@withContext firestore.collection(CHATS_COLLECTION)
            .document(chatId)
            .get()
            .await()
            .let(::getChatFirebaseModel)
    }

    private fun getChatFirebaseModel(document: DocumentSnapshot) = ChatFirebaseModel(
        participants = document.getList("participants")
    ).also { it.id = document.id }
}