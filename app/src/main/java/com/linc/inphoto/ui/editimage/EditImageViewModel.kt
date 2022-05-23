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
import com.linc.inphoto.ui.editimage.model.toUiState
import com.linc.inphoto.ui.imagesticker.model.ImageStickerIntent
import com.linc.inphoto.ui.managepost.model.ManagePostIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditImageViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val mediaRepository: MediaRepository,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel<EditImageUiState>(navContainerHolder) {

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
                    it.copy(imageUri = imageUri ?: Uri.EMPTY, editOperations = editorOperations)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun finishEditing(intent: EditorIntent?) {
        viewModelScope.launch {
            try {
                val imageUri = currentState.imageUri ?: return@launch
                when (intent) {
                    is EditorIntent.NewAvatar -> {
                        router.sendResult(intent.resultKey, imageUri)
                        router.backTo(NavScreen.ProfileSettingsScreen())
                    }
                    is EditorIntent.NewPost -> router.navigateTo(
                        NavScreen.ManagePostScreen(ManagePostIntent.NewPost(imageUri))
                    )
                    is EditorIntent.NewStory ->
                        router.navigateTo(NavScreen.CreateStoryScreen(imageUri))
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun cancelEditing() {
        val imageUri = currentState.imageUri ?: Uri.EMPTY
        mediaRepository.deleteLocalUri(imageUri)
        router.exit()
    }

    private fun selectEditorOperation(operation: EditOperation) {
        val imageUri = currentState.imageUri ?: Uri.EMPTY
        Timber.d(imageUri.toString())
        val operationScreen = when (operation) {
            is EditOperation.Crop -> NavScreen.CropImageScreen(
                CropIntent.Result(EDITOR_OPERATION_RESULT),
                imageUri
            )
            is EditOperation.Sticker -> NavScreen.ImageStickerScreen(
                ImageStickerIntent.Result(EDITOR_OPERATION_RESULT),
                imageUri
            )
            else -> {
                router.showDialog(
                    NavScreen.InfoMessageScreen(
                        resourceProvider.getString(R.string.unavailable),
                        resourceProvider.getString(R.string.unavailable_function_description),
                    )
                )
                return
            }
        }
        router.setResultListener(EDITOR_OPERATION_RESULT) { result ->
            _uiState.update { it.copy(imageUri = Uri.parse(result.toString())) }
        }
        router.navigateTo(operationScreen)
    }

}