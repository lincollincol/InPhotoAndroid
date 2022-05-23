package com.linc.inphoto.ui.storiesoverview

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.linc.inphoto.ui.storiesoverview.model.UserStoryUiState
import com.linc.inphoto.ui.userstories.UserStoriesFragment
import com.linc.inphoto.utils.extensions.update
import com.linc.inphoto.utils.view.viewpager.PagerDiffUtil

class StoriesPageAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    private val storyPages: MutableList<UserStoryUiState> = mutableListOf()

    fun setPages(storyPages: List<UserStoryUiState>) {
        val callback = PagerDiffUtil(this.storyPages, storyPages)
        val diff = DiffUtil.calculateDiff(callback)
        this.storyPages.update(storyPages)
        diff.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = storyPages.count()

    override fun createFragment(position: Int): Fragment {
        return UserStoriesFragment.newInstance(storyPages[position].userId)
    }

}