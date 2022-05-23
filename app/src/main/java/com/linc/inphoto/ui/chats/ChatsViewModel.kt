package com.linc.inphoto.ui.chats

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.ChatRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.entity.chat.Chat
import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.chatmessages.model.UserConversation
import com.linc.inphoto.ui.chats.model.ChatContactUiState
import com.linc.inphoto.ui.chats.model.ChatOperation
import com.linc.inphoto.ui.chats.model.ConversationUiState
import com.linc.inphoto.ui.chats.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.safeCast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository
) : BaseViewModel<ChatsUiState>(navContainerHolder) {

    companion object {
        private const val CHAT_ACTION_RESULT = "chat_action_result"
    }

    override val _uiState = MutableStateFlow(ChatsUiState())
    private var chats: List<ConversationUiState> = listOf()
    private var contacts: List<ChatContactUiState> = listOf()

    fun updateSearchQuery(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                chats = chats.filter { it.username.contains(query, ignoreCase = true) },
                contacts = contacts.filter { it.username.contains(query, ignoreCase = true) }
            )
        }
    }

    fun loadChats() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                launch { loadUserContacts() }
                launch { loadUserChats() }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun loadUserChats() = coroutineScope {
        chatRepository.getUserChats()
            .catch { Timber.e(it) }
            .collect { chats ->
                this@ChatsViewModel.chats = chats.sortedByDescending { it.lastMessageTimestamp }
                    .map(::getConversationUiState)
                removeExistingChatContacts(chats.map { it.userId })
                _uiState.update { it.copy(chats = this@ChatsViewModel.chats) }
            }
    }

    private suspend fun loadUserContacts() = coroutineScope {
        contacts = listOf(
            async { userRepository.loadLoggedInUserFollowers() },
            async { userRepository.loadLoggedInUserFollowing() }
        ).awaitAll()
            .flatMap { it.toList() }
            .toSet()
            .map(::getContactUiState)
        removeExistingChatContacts(chats.map { it.userId })
    }

    private suspend fun removeExistingChatContacts(chatsIds: List<String>) {
        contacts = contacts.filter { !chatsIds.contains(it.userId) }
        _uiState.update { it.copy(contacts = contacts) }
    }

    private fun selectContact(user: User) {
        val conversation = UserConversation.fromUser(user)
        router.navigateTo(NavScreen.ChatMessagesScreen(conversation))
    }

    private fun selectChat(chat: Chat) {
        val conversation = UserConversation.fromChat(chat)
        router.navigateTo(NavScreen.ChatMessagesScreen(conversation))
    }

    private fun selectUserProfile(userId: String) {
        router.navigateTo(NavScreen.ProfileScreen(userId))
    }

    private fun handleChatActions(chat: Chat) {
        router.setResultListener(CHAT_ACTION_RESULT) { result ->
            val operation = result.safeCast<ChatOperation>() ?: return@setResultListener
            when (operation) {
                ChatOperation.Delete -> deleteChat(chat)
                else -> return@setResultListener
            }
        }
        router.showDialog(
            NavScreen.ChooseOptionScreen(
                CHAT_ACTION_RESULT,
                ChatOperation.getChatOperations()
            )
        )
    }

    private fun deleteChat(chat: Chat) {
        viewModelScope.launch {
            try {
                chatRepository.deleteChat(chat.id)
                val user = userRepository.getUserById(chat.userId) ?: return@launch
                if (user.isFollowingUser) {
                    contacts = contacts.toMutableList().apply { add(getContactUiState(user)) }
                    _uiState.update { it.copy(contacts = contacts) }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun getContactUiState(user: User): ChatContactUiState {
        return user.toUiState(
            onClick = { selectContact(user) },
            onUserClick = { selectUserProfile(user.id) }
        )
    }

    private fun getConversationUiState(chat: Chat): ConversationUiState {
        return chat.toUiState(
            onClick = { selectChat(chat) },
            onMenuClick = { handleChatActions(chat) },
            onUserClick = { selectUserProfile(chat.userId) }
        )
    }
}