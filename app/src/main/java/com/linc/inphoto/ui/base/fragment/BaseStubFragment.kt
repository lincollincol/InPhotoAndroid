package com.linc.inphoto.ui.base.fragment

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.EmptyUiState
import com.linc.inphoto.ui.base.viewmodel.BaseStubViewModel
import com.linc.inphoto.ui.base.UiEffect
import com.linc.inphoto.ui.base.UiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseStubFragment<B : ViewBinding, V : BaseViewModel<UiState, UiEffect>> : BaseFragment<B, V, UiState, UiEffect>() {

    override fun handleUiState(state: UiState) = null
    override fun handleUiEffect(effect: UiEffect) = null

//    override fun handleUiEffect(effect: BaseUiEffect) = when(effect) {
//        is BaseUiEffect.Loading -> println("Load")
//        is BaseUiEffect.Error -> showErrorMessage(effect.message)
//    }

}