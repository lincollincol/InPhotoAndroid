package com.linc.inphoto.ui.navigation

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.linc.inphoto.BuildConfig
import com.linc.inphoto.entity.media.LocalMedia
import com.linc.inphoto.ui.auth.signin.SignInFragment
import com.linc.inphoto.ui.auth.signup.SignUpFragment
import com.linc.inphoto.ui.camera.CameraFragment
import com.linc.inphoto.ui.camera.model.CameraIntent
import com.linc.inphoto.ui.chatattachments.ChatAttachmentsFragment
import com.linc.inphoto.ui.chatmessages.ChatMessagesFragment
import com.linc.inphoto.ui.chatmessages.model.ConversationParams
import com.linc.inphoto.ui.chats.ChatsFragment
import com.linc.inphoto.ui.confirmdialog.ConfirmDialog
import com.linc.inphoto.ui.createstory.CreateStoryFragment
import com.linc.inphoto.ui.cropimage.CropImageFragment
import com.linc.inphoto.ui.cropimage.model.CropIntent
import com.linc.inphoto.ui.datepicker.DurationPickerFragment
import com.linc.inphoto.ui.editimage.EditImageFragment
import com.linc.inphoto.ui.editimage.model.EditorIntent
import com.linc.inphoto.ui.feed.FeedFragment
import com.linc.inphoto.ui.filemanager.FileManagerFragment
import com.linc.inphoto.ui.filemanager.model.FileManagerIntent
import com.linc.inphoto.ui.gallery.GalleryFragment
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.helpsettings.HelpSettingsFragment
import com.linc.inphoto.ui.home.HomeFragment
import com.linc.inphoto.ui.imagesticker.ImageStickerFragment
import com.linc.inphoto.ui.imagesticker.model.ImageStickerIntent
import com.linc.inphoto.ui.infodialog.InfoMessageFragment
import com.linc.inphoto.ui.languagesettings.LanguageSettingsFragment
import com.linc.inphoto.ui.main.MainFragment
import com.linc.inphoto.ui.main.MenuTab
import com.linc.inphoto.ui.managepost.ManagePostFragment
import com.linc.inphoto.ui.managepost.model.ManagePostIntent
import com.linc.inphoto.ui.mediareview.MediaReviewFragment
import com.linc.inphoto.ui.optionpicker.OptionPickerFragment
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import com.linc.inphoto.ui.postcomments.PostCommentsFragment
import com.linc.inphoto.ui.postsoverview.PostOverviewFragment
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.ui.profile.ProfileFragment
import com.linc.inphoto.ui.profilefollowers.ProfileFollowersFragment
import com.linc.inphoto.ui.profilefollowers.model.SubscriptionType
import com.linc.inphoto.ui.profilesettings.ProfileSettingsFragment
import com.linc.inphoto.ui.search.SearchFragment
import com.linc.inphoto.ui.settings.SettingsFragment
import com.linc.inphoto.ui.splash.SplashFragment
import com.linc.inphoto.ui.storiesoverview.StoriesOverviewFragment
import com.linc.inphoto.ui.tab.TabFragment
import com.linc.inphoto.ui.tagposts.TagPostsFragment
import com.linc.inphoto.ui.webpage.WebPageFragment
import java.util.*


object NavScreen {

    fun getTabHostScreen(tab: MenuTab?) = when (tab) {
        MenuTab.HOME -> HomeScreen()
        MenuTab.SEARCH -> SearchScreen()
        MenuTab.CHATS -> ChatsScreen()
        else -> ProfileScreen()
    }

    fun TabScreen(tab: MenuTab) = FragmentScreen {
        TabFragment.newInstance(tab)
    }

    fun ChooseOptionScreen(
        resultKey: String,
        options: List<OptionModel>
    ) = DialogScreen {
        OptionPickerFragment.newInstance(resultKey, options)
    }

    fun DatePickerScreen(
        resultKey: String,
        title: String,
        selectedDurationMillis: Long,
        durationMillis: List<Long>
    ) = DialogScreen {
        DurationPickerFragment.newInstance(resultKey, title, selectedDurationMillis, durationMillis)
    }

    fun InfoMessageScreen(
        title: String,
        message: String,
        resultKey: String? = null
    ) = DialogScreen {
        InfoMessageFragment.newInstance(resultKey, title, message)
    }

    fun ConfirmDialogScreen(
        resultKey: String,
        title: String,
        message: String,
    ) = DialogScreen {
        ConfirmDialog.newInstance(resultKey, title, message)
    }

    fun SignInScreen() = FragmentScreen {
        SignInFragment.newInstance()
    }

    fun SignUpScreen() = FragmentScreen {
        SignUpFragment.newInstance()
    }

    fun CameraScreen(intent: CameraIntent) = FragmentScreen {
        CameraFragment.newInstance(intent)
    }

    fun GalleryScreen(intent: GalleryIntent) = FragmentScreen {
        GalleryFragment.newInstance(intent)
    }

    fun AudioLibraryScreen(
        mimeType: String,
        intent: FileManagerIntent
    ) = FragmentScreen {
        FileManagerFragment.newInstance(mimeType, intent)
    }

    fun MediaReviewScreen(files: List<Uri>) = FragmentScreen {
        MediaReviewFragment.newInstance(files)
    }

    fun EditImageScreen(intent: EditorIntent, image: Uri) = FragmentScreen {
        EditImageFragment.newInstance(intent, image)
    }

    fun CropImageScreen(intent: CropIntent, image: Uri) = FragmentScreen {
        CropImageFragment.newInstance(intent, image)
    }

    fun ImageStickerScreen(intent: ImageStickerIntent, image: Uri) = FragmentScreen {
        ImageStickerFragment.newInstance(intent, image)
    }

    fun ManagePostScreen(intent: ManagePostIntent) = FragmentScreen {
        ManagePostFragment.newInstance(intent)
    }

    fun SplashScreen() = FragmentScreen {
        SplashFragment.newInstance()
    }

    fun MainScreen() = FragmentScreen {
        MainFragment.newInstance()
    }

    fun ProfileScreen(userId: String? = null) = FragmentScreen(key = UUID.randomUUID().toString()) {
        ProfileFragment.newInstance(userId)
    }

    fun PostOverviewScreen(overviewType: OverviewType) = FragmentScreen {
        PostOverviewFragment.newInstance(overviewType)
    }

    fun HomeScreen() = FragmentScreen {
        HomeFragment.newInstance()
    }

    fun CreateStoryScreen(contentUri: Uri) = FragmentScreen {
        CreateStoryFragment.newInstance(contentUri)
    }

    fun StoriesOverviewScreen(initialUserId: String) = FragmentScreen {
        StoriesOverviewFragment.newInstance(initialUserId)
    }

    fun FeedScreen() = FragmentScreen {
        FeedFragment.newInstance()
    }

    fun ChatsScreen() = FragmentScreen {
        ChatsFragment.newInstance()
    }

    fun ChatMessagesScreen(conversation: ConversationParams) = FragmentScreen {
        ChatMessagesFragment.newInstance(conversation)
    }

    fun MessageAttachmentsScreen(
        resultKey: String,
        chatId: String?,
        receiverId: String?,
        attachments: List<LocalMedia>
    ) = DialogScreen {
        ChatAttachmentsFragment.newInstance(resultKey, chatId, receiverId, attachments)
    }

    fun SettingsScreen() = FragmentScreen {
        SettingsFragment.newInstance()
    }

    fun ProfileSettingsScreen() = FragmentScreen {
        ProfileSettingsFragment.newInstance()
    }

    fun HelpSettingsScreen() = FragmentScreen {
        HelpSettingsFragment.newInstance()
    }

    fun WebPageScreen(
        title: String,
        pageUrl: String
    ) = FragmentScreen {
        WebPageFragment.newInstance(title, pageUrl)
    }

    fun LanguageSettingsScreen() = FragmentScreen {
        LanguageSettingsFragment.newInstance()
    }

    fun PostCommentsScreen(postId: String) = FragmentScreen {
        PostCommentsFragment.newInstance(postId)
    }

    fun SearchScreen() = FragmentScreen {
        SearchFragment.newInstance()
    }

    fun TagPostsScreen(tagId: String) = FragmentScreen {
        TagPostsFragment.newInstance(tagId)
    }

    fun ProfileFollowersScreen(
        userId: String?,
        subscriptionType: SubscriptionType
    ) = FragmentScreen(key = UUID.randomUUID().toString()) {
        ProfileFollowersFragment.newInstance(userId, subscriptionType)
    }

    /**
     * External navigation
     */
    fun ShareContentScreen(content: String) = ActivityScreen {
        val contentIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            type = "text/plain"
        }
        Intent.createChooser(contentIntent, null)
    }

    fun AppSettingsScreen() = ActivityScreen {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
        )
    }
}