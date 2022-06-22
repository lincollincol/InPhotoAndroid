package com.linc.inphoto.ui.filemanager

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
import com.linc.inphoto.databinding.FragmentFileManagerBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.filemanager.item.FileItem
import com.linc.inphoto.ui.filemanager.model.FileManagerIntent
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.*
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class FileManagerFragment : BaseFragment(R.layout.fragment_file_manager) {

    companion object {
        private const val MIME_TYPE_ARG = "mime"
        private const val INTENT_ARG = "intent"

        @JvmStatic
        fun newInstance(
            mimeType: String,
            intent: FileManagerIntent
        ) = FileManagerFragment().apply {
            arguments = bundleOf(
                MIME_TYPE_ARG to mimeType,
                INTENT_ARG to intent
            )
        }
    }

    override val viewModel: FileManagerViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentFileManagerBinding::bind)
    private val audiosSection by lazy { Section() }

    private val permissionsResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { allowed ->
        when {
            allowed -> viewModel.loadFiles(
                getArgumentNotNull(MIME_TYPE_ARG),
                getArgument(INTENT_ARG)
            )
            else -> viewModel.permissionDenied()
        }
    }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            toolbarView.setDoneVisible(state.allowMultipleSelection)
            audiosSection.replaceAll(state.files.map(::FileItem))
            audiosRecyclerView.show(state.storagePermissionsGranted)
            permissionsLayout.root.show(!state.storagePermissionsGranted)
            progressBar.show(state.isLoading && state.files.isEmpty())
            notFoundLayout.root.show(!state.isLoading && state.files.isEmpty())
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
            toolbarView.apply {
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