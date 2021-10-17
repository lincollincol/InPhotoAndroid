package com.linc.inphoto.ui

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.linc.inphoto.ui.auth.sign_in.SignInFragment
import com.linc.inphoto.ui.auth.sign_up.SignUpFragment
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

}