package com.linc.inphoto.ui.mediareview

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.linc.inphoto.ui.mediareview.model.MediaFileUiState
import com.linc.inphoto.utils.extensions.isVideoMimeType
import com.linc.inphoto.utils.extensions.update
import com.linc.inphoto.utils.view.viewpager.PagerDiffUtil

class MediaPageAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    private val mediaPages: MutableList<MediaFileUiState> = mutableListOf()

    fun setPages(mediaPages: List<MediaFileUiState>) {
        val callback = PagerDiffUtil(this.mediaPages, mediaPages)
        val diff = DiffUtil.calculateDiff(callback)
        this.mediaPages.update(mediaPages)
        diff.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = mediaPages.count()

    override fun createFragment(position: Int): Fragment {
        val media = mediaPages[position].media
        return when {
            media.mimeType.isVideoMimeType() -> VideoOverviewFragment.newInstance(media.uri)
            else -> ImageOverviewFragment.newInstance(media.uri)
        }
    }

}