package com.linc.inphoto.ui.editimage

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.SettingsRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.editimage.model.EditOperation
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditImageViewModel @Inject constructor(
    router: Router,
    private val settingsRepository: SettingsRepository
) : BaseViewModel<EditImageUiState>(router) {

    override val _uiState = MutableStateFlow(EditImageUiState())

    fun loadOperations() {
        viewModelScope.launch {
            try {
                _uiState.update {
                    copy(editOperations = listOf(EditOperation.Crop, EditOperation.Filter))
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }


}