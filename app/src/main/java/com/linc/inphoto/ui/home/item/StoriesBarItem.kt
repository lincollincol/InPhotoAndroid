package com.linc.inphoto.ui.home.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemStoriesBarBinding
import com.linc.inphoto.ui.home.model.StoriesUiState
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.view.horizontalLinearLayoutManager
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator

class StoriesBarItem(
    private val storiesUiState: StoriesUiState
) : BindableItem<ItemStoriesBarBinding>(12345) {
    override fun bind(viewBinding: ItemStoriesBarBinding, position: Int) {
        with(viewBinding) {
            val storiesSection = Section(
                storiesUiState.newStory?.let(::NewStoryItem),
                storiesUiState.stories.map(::SingleStoryItem)
            )
            storiesRecyclerView.apply {
                layoutManager = horizontalLinearLayoutManager()
                adapter = createAdapter(storiesSection)
                itemAnimator = FadeInLeftAnimator()
                show(storiesUiState.newStory != null)
            }
            separatorView.show(storiesUiState.newStory != null)
        }
    }

    override fun getLayout(): Int = R.layout.item_stories_bar

    override fun initializeViewBinding(view: View) = ItemStoriesBarBinding.bind(view)
}