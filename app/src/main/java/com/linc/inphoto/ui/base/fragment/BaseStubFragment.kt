package com.linc.inphoto.ui.base.fragment

import androidx.viewbinding.ViewBinding
import com.linc.inphoto.ui.base.viewmodel.BaseStubViewModel
import com.linc.inphoto.ui.base.UiEffect
import com.linc.inphoto.ui.base.UiState
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseStubFragment<B : ViewBinding, V : BaseStubViewModel> : BaseFragment<B, V, UiState, UiEffect>() {

    override fun handleUiState(state: UiState) = null

    override fun handleUiEffect(effect: UiEffect) = null

}