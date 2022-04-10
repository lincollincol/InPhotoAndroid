package com.linc.inphoto.ui.editimage

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.cropimage.model.CropIntent
import com.linc.inphoto.ui.editimage.model.EditOperation
import com.linc.inphoto.ui.editimage.model.EditorIntent
import com.linc.inphoto.ui.managepost.model.ManageablePost
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditImageViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val mediaRepository: MediaRepository,
    private val userRepository: UserRepository,
) : BaseViewModel<EditImageUiState>(navContainerHolder) {

    companion object {
        private const val EDITOR_OPERATION_RESULT = "editor_result"
    }

    override val _uiState = MutableStateFlow(EditImageUiState())
    private var intent: EditorIntent? = null

    fun applyImage(intent: EditorIntent?, imageUri: Uri?) {
        this.intent = intent
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
        viewModelScope.launch {
            try {
                val imageUri = uiState.value.imageUri ?: return@launch
                when (intent) {
                    EditorIntent.NewAvatar -> {
                        userRepository.updateUserAvatar(imageUri)
                        router.backTo(NavScreen.ProfileScreen())
                    }
                    EditorIntent.NewPost -> {
                        router.navigateTo(NavScreen.ManagePostScreen(ManageablePost(imageUri)))
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun cancelEditing() {
        val imageUri = _uiState.value.imageUri ?: Uri.EMPTY
        mediaRepository.deleteLocalUri(imageUri)
        router.exit()
    }

    private fun selectEditorOperation(operation: EditOperation) {
        val imageUri = _uiState.value.imageUri ?: Uri.EMPTY
        Timber.d(imageUri.toString())
        val operationScreen = when (operation) {
            is EditOperation.Crop -> NavScreen.CropImageScreen(
                CropIntent.Result(EDITOR_OPERATION_RESULT),
                imageUri
            )
            else -> {
                showInfo(R.string.unavailable, R.string.unavailable_function_description)
                return
            }
        }
        router.setResultListener(EDITOR_OPERATION_RESULT) { result ->
            _uiState.update { copy(imageUri = Uri.parse(result.toString())) }
        }
        router.navigateTo(operationScreen)
    }

}