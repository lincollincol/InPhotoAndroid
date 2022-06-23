package com.linc.inphoto.ui.mediareview

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.mediareview.model.MediaFileUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.utils.extensions.mapAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MediaReviewViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val mediaRepository: MediaRepository
) : BaseViewModel<MediaReviewUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(MediaReviewUiState())

    fun applyMediaFiles(files: List<Uri>) {
        viewModelScope.launch {
            try {
                val mediaFiles = files.mapAsync(mediaRepository::getMediaFromUri)
                    .awaitAll()
                    .filterNotNull()
                    .map(::MediaFileUiState)
                _uiState.update { it.copy(files = mediaFiles) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

}