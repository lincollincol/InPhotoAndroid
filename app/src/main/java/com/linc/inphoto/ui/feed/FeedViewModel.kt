package com.linc.inphoto.ui.feed

import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<FeedUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(FeedUiState())

    fun search() {
        router.navigateTo(NavScreen.SearchScreen())
    }

}