package com.linc.inphoto.ui.navigation

import android.net.Uri
import androidx.annotation.StringRes
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.linc.inphoto.ui.auth.signin.SignInFragment
import com.linc.inphoto.ui.auth.signup.SignUpFragment
import com.linc.inphoto.ui.camera.CameraFragment
import com.linc.inphoto.ui.cropimage.CropImageFragment
import com.linc.inphoto.ui.editimage.EditImageFragment
import com.linc.inphoto.ui.gallery.GalleryFragment
import com.linc.inphoto.ui.infodialog.InfoMessageDialog
import com.linc.inphoto.ui.optionpicker.OptionPickerFragment
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import com.linc.inphoto.ui.profile.ProfileFragment
import com.linc.inphoto.ui.splash.SplashFragment

object Navigation {

    object Common {
        fun ChooseOptionScreen(
            resultKey: String,
            options: List<OptionModel>
        ) = FragmentScreen(clearContainer = false) {
            OptionPickerFragment.newInstance(resultKey, options)
        }

        fun InfoMessageScreen(
            @StringRes title: Int,
            @StringRes message: Int
        ) = FragmentScreen(clearContainer = false) {
            InfoMessageDialog.newInstance(title, message)
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

    object ImageModule {
        fun CameraScreen(resultKey: String) = FragmentScreen {
            CameraFragment.newInstance(resultKey)
        }

        fun GalleryScreen(resultKey: String) = FragmentScreen {
            GalleryFragment.newInstance(resultKey)
        }

        fun EditImageScreen(resultKey: String, image: Uri) = FragmentScreen {
            EditImageFragment.newInstance(image)
        }

        fun CropImageScreen(resultKey: String, image: Uri) = FragmentScreen {
            CropImageFragment.newInstance(resultKey, image)
        }
    }

    fun SplashScreen() = FragmentScreen {
        SplashFragment.newInstance()
    }

    fun ProfileScreen() = FragmentScreen {
        ProfileFragment.newInstance()
    }

}