package com.linc.inphoto.ui.navigation

import androidx.annotation.StringRes
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.linc.inphoto.ui.auth.signin.SignInFragment
import com.linc.inphoto.ui.auth.signup.SignUpFragment
import com.linc.inphoto.ui.camera.CameraFragment
import com.linc.inphoto.ui.gallery.GalleryFragment
import com.linc.inphoto.ui.infodialog.InfoMessageDialog
import com.linc.inphoto.ui.optionpicker.OptionPickerFragment
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import com.linc.inphoto.ui.profile.ProfileFragment
import com.linc.inphoto.ui.splash.SplashFragment

object Navigation {

    object NavResult {
        const val CHOOSE_OPTION_RESULT = "choose_result"
        const val CAMERA_IMAGE_RESULT = "camera_image_result"

    }

    object Common {
        fun ChooseOptionScreen(
            options: List<OptionModel>
        ) = FragmentScreen(
            key = NavResult.CHOOSE_OPTION_RESULT,
            clearContainer = false
        ) {
            OptionPickerFragment.newInstance(options)
        }

        fun InfoMessageScreen(
            @StringRes title: Int,
            @StringRes message: Int
        ) = FragmentScreen(clearContainer = false) {
            InfoMessageDialog.newInstance(title, message)
        }

        fun CameraScreen() = FragmentScreen {
            CameraFragment.newInstance()
        }

        fun GalleryScreen() = FragmentScreen {
            GalleryFragment.newInstance()
        }
    }

    object Auth {
        fun SignInScreen() = FragmentScreen {
            SignInFragment.newInstance()
        }

        fun SignUpScreen() = FragmentScreen {
            SignUpFragment.newInstance()
        }
    }

    fun SplashScreen() = FragmentScreen {
        SplashFragment.newInstance()
    }

    fun ProfileScreen() = FragmentScreen {
        ProfileFragment.newInstance()
    }

}