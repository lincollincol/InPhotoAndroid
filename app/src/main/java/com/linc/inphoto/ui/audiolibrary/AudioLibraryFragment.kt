package com.linc.inphoto.ui.audiolibrary

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentAudioLibraryBinding
import com.linc.inphoto.ui.audiolibrary.item.AudioItem
import com.linc.inphoto.ui.audiolibrary.model.AudioLibraryIntent
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
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
    private val binding by viewBinding(FragmentAudioLibraryBinding::bind)
    private val audiosSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
//            audiosSection.update(state.audios.map(::AudioItem))
            audiosSection.replaceAll(state.audios.map(::AudioItem))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadAudioList()
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
            audiosToolbarView.setOnCancelClickListener {
                viewModel.onBackPressed()
            }
        }
    }

}