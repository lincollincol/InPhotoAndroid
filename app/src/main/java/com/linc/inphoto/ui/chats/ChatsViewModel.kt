package com.linc.inphoto.ui.chats

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.ChatRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.entity.chat.Chat
import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.chatmessages.model.UserConversation
import com.linc.inphoto.ui.chats.model.ChatContactUiState
import com.linc.inphoto.ui.chats.model.ConversationUiState
import com.linc.inphoto.ui.chats.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
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


    /**
     * TODO
     * chats updates
     */

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
                loadUserChats()
                loadUserContacts()
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun loadUserChats() = coroutineScope {
        chats = chatRepository.loadUserChats()
            .map { it.toUiState { selectChat(it) } }
        _uiState.update { it.copy(chats = chats) }
    }

    private suspend fun loadUserContacts() = coroutineScope {
        contacts = listOf(
            async { userRepository.loadLoggedInUserFollowers() },
            async { userRepository.loadLoggedInUserFollowing() }
        ).awaitAll()
            .flatMap { it.toList() }
            .filter { user -> chats.find { it.userId == user.id } == null }
            .toSet()
            .map { it.toUiState { selectContact(it) } }
        _uiState.update { it.copy(contacts = contacts) }
    }

    private fun selectChat(chat: Chat) {
        val conversation = UserConversation.fromChat(chat)
        router.navigateTo(NavScreen.ChatMessagesScreen(conversation))
    }

    private fun selectContact(user: User) {
        val conversation = UserConversation.fromUser(user)
        router.navigateTo(NavScreen.ChatMessagesScreen(conversation))
    }
}