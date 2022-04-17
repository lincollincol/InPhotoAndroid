package com.linc.inphoto.ui.navigation

import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.linc.inphoto.ui.auth.signin.SignInFragment
import com.linc.inphoto.ui.auth.signup.SignUpFragment
import com.linc.inphoto.ui.camera.CameraFragment
import com.linc.inphoto.ui.camera.model.CameraIntent
import com.linc.inphoto.ui.chats.ChatsFragment
import com.linc.inphoto.ui.confirmdialog.ConfirmDialog
import com.linc.inphoto.ui.cropimage.CropImageFragment
import com.linc.inphoto.ui.cropimage.model.CropIntent
import com.linc.inphoto.ui.editimage.EditImageFragment
import com.linc.inphoto.ui.editimage.model.EditorIntent
import com.linc.inphoto.ui.feed.FeedFragment
import com.linc.inphoto.ui.gallery.GalleryFragment
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.home.HomeFragment
import com.linc.inphoto.ui.infodialog.InfoMessageFragment
import com.linc.inphoto.ui.main.MainFragment
import com.linc.inphoto.ui.main.MenuTab
import com.linc.inphoto.ui.managepost.ManagePostFragment
import com.linc.inphoto.ui.managepost.model.ManagePostIntent
import com.linc.inphoto.ui.optionpicker.OptionPickerFragment
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import com.linc.inphoto.ui.postsoverview.PostOverviewFragment
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.ui.profile.ProfileFragment
import com.linc.inphoto.ui.profilesettings.ProfileSettingsFragment
import com.linc.inphoto.ui.settings.SettingsFragment
import com.linc.inphoto.ui.splash.SplashFragment
import com.linc.inphoto.ui.tab.TabFragment

object NavScreen {

    fun getTabHostScreen(tab: MenuTab?) = when (tab) {
        MenuTab.HOME -> HomeScreen()
        MenuTab.FEED -> FeedScreen()
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

    fun InfoMessageScreen(
        @StringRes title: Int,
        @StringRes message: Int,
        resultKey: String? = null
    ) = DialogScreen {
        InfoMessageFragment.newInstance(title, message, resultKey)
    }

    fun ConfirmDialogScreen(
        resultKey: String,
        @StringRes title: Int,
        @StringRes message: Int
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

    fun EditImageScreen(intent: EditorIntent, image: Uri) = FragmentScreen {
        EditImageFragment.newInstance(intent, image)
    }

    fun CropImageScreen(intent: CropIntent, image: Uri) = FragmentScreen {
        CropImageFragment.newInstance(intent, image)
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

    fun ProfileScreen() = FragmentScreen {
        ProfileFragment.newInstance()
    }

    fun PostOverviewScreen(overviewType: OverviewType) = FragmentScreen {
        PostOverviewFragment.newInstance(overviewType)
    }

    fun HomeScreen() = FragmentScreen {
        HomeFragment.newInstance()
    }

    fun FeedScreen() = FragmentScreen {
        FeedFragment.newInstance()
    }

    fun ChatsScreen() = FragmentScreen {
        ChatsFragment.newInstance()
    }

    fun SettingsScreen() = FragmentScreen {
        SettingsFragment.newInstance()
    }

    fun ProfileSettingsScreen() = FragmentScreen {
        ProfileSettingsFragment.newInstance()
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
}