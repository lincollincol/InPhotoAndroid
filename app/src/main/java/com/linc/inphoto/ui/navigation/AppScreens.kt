package com.linc.inphoto.ui.navigation

import androidx.annotation.StringRes
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.linc.inphoto.ui.auth.signin.SignInFragment
import com.linc.inphoto.ui.auth.signup.SignUpFragment
import com.linc.inphoto.ui.choosedialog.ChooseOptionFragment
import com.linc.inphoto.ui.choosedialog.model.ChooseOptionModel
import com.linc.inphoto.ui.infodialog.InfoMessageDialog
import com.linc.inphoto.ui.profile.ProfileFragment
import com.linc.inphoto.ui.splash.SplashFragment

object AppScreens {

    fun SplashScreen() = FragmentScreen {
        SplashFragment.newInstance()
    }

    fun SignInScreen() = FragmentScreen {
        SignInFragment.newInstance()
    }

    fun SignUpScreen() = FragmentScreen {
        SignUpFragment.newInstance()
    }

    fun ProfileScreen() = FragmentScreen {
        ProfileFragment.newInstance()
    }

    fun ChooseOptionScreen(options: List<ChooseOptionModel>) = FragmentScreen(
        key = ScreenResultKey.CHOOSE_OPTION_RESULT,
        clearContainer = false
    ) {
        ChooseOptionFragment.newInstance(options)
    }

    fun InfoMessageScreen(
        @StringRes title: Int,
        @StringRes message: Int
    ) = FragmentScreen(clearContainer = false) {
        InfoMessageDialog.newInstance(title, message)
    }

}