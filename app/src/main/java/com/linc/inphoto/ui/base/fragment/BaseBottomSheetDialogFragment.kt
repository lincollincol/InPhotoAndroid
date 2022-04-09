package com.linc.inphoto.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.linc.inphoto.R
import com.linc.inphoto.ui.navigation.FragmentBackPressedListener

abstract class BaseBottomSheetDialogFragment(
    @LayoutRes private val layoutId: Int
) : BottomSheetDialogFragment(), FragmentBackPressedListener {

    protected abstract suspend fun observeUiState()

    override fun getTheme(): Int {
        return R.style.TopRoundedBottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted { observeUiState() }
    }

    override fun onBackPressed() {
    }

}