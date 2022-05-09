package com.linc.inphoto.ui.chatmessages.item

import android.util.Size
import android.view.View
import androidx.core.view.doOnLayout
import androidx.transition.Fade
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemMessageIncomingBinding
import com.linc.inphoto.ui.chatmessages.model.MessageUiState
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.pattern.TIME_PATTERN_SEMICOLON
import com.linc.inphoto.utils.extensions.view.animateTargets
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.glide.LoadingListener
import com.xwray.groupie.viewbinding.BindableItem
import java.util.*

class IncomingMessageItem(
    private val messageUiState: MessageUiState
) : BindableItem<ItemMessageIncomingBinding>(messageUiState.id.hashCode().toLong()) {
    override fun bind(viewBinding: ItemMessageIncomingBinding, position: Int) {
        with(viewBinding) {
            messageTextView.text = messageUiState.text
            timeTextView.text = DateFormatter.format(
                messageUiState.createdTimestamp,
                TIME_PATTERN_SEMICOLON,
                Locale.US
            )
            fileImageView.setOnThrottledClickListener {
                messageUiState.onImageClick()
            }
            showAllButton.apply {
                show(messageUiState.files.count() > 2)
                setOnThrottledClickListener {
                    messageUiState.onImageClick()
                }
            }
            root.apply {
                doOnLayout {
                    fileImageView.loadImage(
                        messageUiState.files.firstOrNull(),
                        size = Size(it.width, it.width),
                        listener = LoadingListener {
                            animateTargets(Fade(Fade.IN), fileImageView)
                            fileImageView.show(messageUiState.files.isNotEmpty())
                        }
                    )
                }
                setOnThrottledClickListener {
                    messageUiState.onClick()
                }
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_message_incoming

    override fun initializeViewBinding(view: View) = ItemMessageIncomingBinding.bind(view)
}