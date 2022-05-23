package com.linc.inphoto.ui.storiesoverview

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.linc.inphoto.ui.storiesoverview.model.UserStoryUiState
import com.linc.inphoto.ui.userstories.UserStoriesFragment
import com.linc.inphoto.utils.extensions.update

class StoriesPageAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    private val storyPages: MutableList<UserStoryUiState> = mutableListOf()

    fun setPages(storyPages: List<UserStoryUiState>) {
        this.storyPages.update(storyPages)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = storyPages.count()
    override fun createFragment(position: Int): Fragment {
        return UserStoriesFragment.newInstance(storyPages[position].userId)
    }

}