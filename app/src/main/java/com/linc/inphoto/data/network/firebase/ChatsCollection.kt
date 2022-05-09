package com.linc.inphoto.data.network.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.linc.inphoto.data.network.model.chat.ChatFirebaseModel
import com.linc.inphoto.utils.getList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatsCollection @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        private const val COLLECTION_NAME = "chats"
    }

    suspend fun loadUserChats(userId: String): List<ChatFirebaseModel> = withContext(ioDispatcher) {
        return@withContext firestore.collection(COLLECTION_NAME)
            .whereArrayContains("participants", userId)
            .get()
            .await()
            .map { ChatFirebaseModel(it.id, it.getList("participants")) }
    }

    suspend fun loadChat(chatId: String): ChatFirebaseModel = withContext(ioDispatcher) {
        return@withContext firestore.collection(COLLECTION_NAME)
            .document(chatId)
            .get()
            .await()
            .let { ChatFirebaseModel(it.id, it.getList("participants")) }
    }
}