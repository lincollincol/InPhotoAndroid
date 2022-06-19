package com.linc.inphoto.ui.audiolibrary

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentAudioLibraryBinding
import com.linc.inphoto.ui.audiolibrary.item.AudioItem
import com.linc.inphoto.ui.audiolibrary.model.AudioLibraryIntent
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.permissionDisabled
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class AudioLibraryFragment : BaseFragment(R.layout.fragment_audio_library) {

    companion object {
        private const val INTENT_ARG = "intent"

        @JvmStatic
        fun newInstance(
            intent: AudioLibraryIntent
        ) = AudioLibraryFragment().apply {
            arguments = bundleOf(INTENT_ARG to intent)
        }
    }

    override val viewModel: AudioLibraryViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentAudioLibraryBinding::bind)
    private val audiosSection by lazy { Section() }

    private val permissionsResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { allowed ->
        when {
            allowed -> viewModel.loadAudioList(getArgument(INTENT_ARG))
            else -> viewModel.permissionDenied()
        }
    }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            audiosSection.replaceAll(state.audios.map(::AudioItem))
//            audiosSection.update(state.audios.map(::AudioItem))
            audiosRecyclerView.show(state.storagePermissionsGranted)
            audiosToolbarView.setDoneVisible(state.allowMultipleSelection)
            permissionsLayout.root.show(!state.storagePermissionsGranted)
            progressBar.show(state.isLoading && state.audios.isEmpty())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            audiosRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(audiosSection)
                itemAnimator = FadeInDownAnimator()
            }
            searchEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateSearchQuery(text.toString())
            }
            audiosToolbarView.apply {
                setOnDoneClickListener { viewModel.confirmSelectedAudios() }
                setOnCancelClickListener { viewModel.onBackPressed() }
            }
            permissionsLayout.apply {
                permissionTextView.setText(R.string.permission_audio_description)
                allowButton.setOnClickListener {
                    val permission = Manifest.permission.READ_EXTERNAL_STORAGE
                    if (permissionDisabled(permission)) {
                        viewModel.openSettings()
                        return@setOnClickListener
                    }
                    permissionsResult.launch(permission)
                }
            }
            enterTransition = Fade(Fade.IN)
            reenterTransition = enterTransition
        }
        bottomBarViewModel.hideBottomBar()
    }

    override fun onStart() {
        super.onStart()
        permissionsResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

}