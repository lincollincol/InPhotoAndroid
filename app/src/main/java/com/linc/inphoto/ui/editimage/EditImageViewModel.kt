package com.linc.inphoto.ui.editimage

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.SettingsRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.editimage.model.EditOperation
import com.linc.inphoto.ui.navigation.Navigation
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

    companion object {
        private const val EDITOR_OPERATION_RESULT = "editor_result"
    }

    override val _uiState = MutableStateFlow(EditImageUiState())

    fun applyImage(imageUri: Uri?) {
        viewModelScope.launch {
            try {
                val editorOperations = EditOperation.getEditorOperations()
                    .map { it.toUiState { selectEditorOperation(it) } }

                _uiState.update {
                    copy(
                        imageUri = imageUri ?: Uri.EMPTY,
                        editOperations = editorOperations
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun finishEditing() {

    }

    fun cancelEditing() {

    }

    private fun selectEditorOperation(operation: EditOperation) {
        val imageUri = _uiState.value.imageUri ?: Uri.EMPTY
        Timber.d(imageUri.toString())
        val operationScreen = when (operation) {
            is EditOperation.Crop -> Navigation.ImageModule.CropImageScreen(
                EDITOR_OPERATION_RESULT,
                imageUri
            )
            else -> return
        }
        router.setResultListener(EDITOR_OPERATION_RESULT) { result ->
            _uiState.update { copy(imageUri = Uri.parse(result.toString())) }
        }
        router.navigateTo(operationScreen)
    }

}