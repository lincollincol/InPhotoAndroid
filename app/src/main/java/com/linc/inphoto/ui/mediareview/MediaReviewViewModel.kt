package com.linc.inphoto.ui.mediareview

import android.net.Uri
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.mediareview.model.MediaFileUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MediaReviewViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<MediaReviewUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(MediaReviewUiState())

    fun applyMediaFiles(files: List<Uri>) {
        _uiState.update { it.copy(files = files.map(::MediaFileUiState)) }
    }

}