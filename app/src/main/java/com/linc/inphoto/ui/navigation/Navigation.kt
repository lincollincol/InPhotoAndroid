package com.linc.inphoto.ui.navigation

import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.linc.inphoto.ui.auth.signin.SignInFragment
import com.linc.inphoto.ui.auth.signup.SignUpFragment
import com.linc.inphoto.ui.camera.CameraFragment
import com.linc.inphoto.ui.cropimage.CropImageFragment
import com.linc.inphoto.ui.editimage.EditImageFragment
import com.linc.inphoto.ui.gallery.GalleryFragment
import com.linc.inphoto.ui.infodialog.InfoMessageFragment
import com.linc.inphoto.ui.managepost.ManagePostFragment
import com.linc.inphoto.ui.managepost.model.ManageablePost
import com.linc.inphoto.ui.optionpicker.OptionPickerFragment
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import com.linc.inphoto.ui.postsoverview.PostOverviewFragment
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.ui.profile.ProfileFragment
import com.linc.inphoto.ui.splash.SplashFragment

object Navigation {

    fun ChooseOptionScreen(
        resultKey: String,
        options: List<OptionModel>
    ) = FragmentScreen(clearContainer = false) {
        OptionPickerFragment.newInstance(resultKey, options)
    }

    fun InfoMessageScreen(
        @StringRes title: Int,
        @StringRes message: Int,
        resultKey: String? = null
    ) = FragmentScreen(clearContainer = false) {
        InfoMessageFragment.newInstance(title, message, resultKey)
    }

    fun SignInScreen() = FragmentScreen {
        SignInFragment.newInstance()
    }

    fun SignUpScreen() = FragmentScreen {
        SignUpFragment.newInstance()
    }

    fun CameraScreen(resultKey: String) = FragmentScreen {
        CameraFragment.newInstance(resultKey)
    }

    fun GalleryScreen(resultKey: String) = FragmentScreen {
        GalleryFragment.newInstance(resultKey)
    }

    fun EditImageScreen(resultKey: String, image: Uri) = FragmentScreen {
        EditImageFragment.newInstance(resultKey, image)
    }

    fun CropImageScreen(resultKey: String, image: Uri) = FragmentScreen {
        CropImageFragment.newInstance(resultKey, image)
    }

    fun ManagePostScreen(post: ManageablePost) = FragmentScreen {
        ManagePostFragment.newInstance(post)
    }

    fun SplashScreen() = FragmentScreen {
        SplashFragment.newInstance()
    }

    fun ProfileScreen() = FragmentScreen {
        ProfileFragment.newInstance()
    }

    fun PostOverviewScreen(overviewType: OverviewType) = FragmentScreen {
        PostOverviewFragment.newInstance(overviewType)
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