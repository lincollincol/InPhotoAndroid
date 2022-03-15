package com.linc.inphoto.ui.managepost

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ManagePostViewModel @Inject constructor(
    router: Router
) : BaseViewModel<ManagePostUiState>(router) {

    override val _uiState = MutableStateFlow(ManagePostUiState())

    fun addTags(tag: String) {
        val tags = uiState.value.tags.toTypedArray()
        _uiState.update { copy(tags = listOf(*tags, tag)) }
    }

    fun removeTags(tag: String) {
        val tags = uiState.value.tags.toMutableList()
        tags.remove(tag)
        _uiState.update { copy(tags = tags) }
    }

    fun updateDescription(description: String) {
        _uiState.update { copy(description = description) }
    }

    fun savePost() {

    }

    fun cancelPost() {
        router.exit()
    }

}