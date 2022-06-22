package com.linc.inphoto.ui.filemanager

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.entity.media.LocalMedia
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.filemanager.model.FileManagerIntent
import com.linc.inphoto.ui.filemanager.model.FileUiState
import com.linc.inphoto.ui.filemanager.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.mapIf
import com.linc.inphoto.utils.extensions.safeCast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FileManagerViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val mediaRepository: MediaRepository
) : BaseViewModel<FileManagerUiState>(navContainerHolder) {

    companion object {
        private const val SEARCH_DEBOUNCE_TIME = 500L
    }

    override val _uiState = MutableStateFlow(FileManagerUiState())
    private var files: List<FileUiState> = listOf()
    private var searchJob: Job? = null
    private var intent: FileManagerIntent? = null

    fun loadFiles(
        mimeType: String,
        intent: FileManagerIntent?
    ) {
        this.intent = intent
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        allowMultipleSelection = intent is FileManagerIntent.MultipleResult
                    )
                }
                files = mediaRepository.loadLocalFiles(mimeType)
                    .map { it.toUiState { selectAudio(it) } }
                _uiState.update { it.copy(files = files) }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_TIME)
            _uiState.update { it.copy(searchQuery = query) }
            filterAudiosToQuery()
        }
    }

    fun confirmSelectedAudios() {
        val intent = intent?.safeCast<FileManagerIntent.MultipleResult>() ?: return
        val result = files.filter { it.isSelected }.map { it.localMedia }
        router.exit()
        router.sendResult(intent.resultKey, result)
    }

    private fun selectAudio(localMedia: LocalMedia) {
        val intent = intent ?: return
        if (intent is FileManagerIntent.SingleResult) {
            router.exit()
            router.sendResult(intent.resultKey, localMedia)
            return
        } else if (intent is FileManagerIntent.MultipleResult) {
            files = files.mapIf(
                condition = { it.localMedia == localMedia },
                transform = { it.copy(isSelected = !it.isSelected) }
            )
            filterAudiosToQuery()
        }
    }

    private fun filterAudiosToQuery() {
        viewModelScope.launch {
            try {
                val query = currentState.searchQuery
                val foundAudios = when {
                    query.isNullOrEmpty() -> files
                    else -> files.filter {
                        it.localMedia.name.contains(query.orEmpty(), ignoreCase = true)
                    }
                }
                _uiState.update { it.copy(files = foundAudios) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun permissionDenied() {
        _uiState.update { it.copy(storagePermissionsGranted = false) }
    }

    fun openSettings() {
        router.navigateTo(NavScreen.AppSettingsScreen())
    }

}