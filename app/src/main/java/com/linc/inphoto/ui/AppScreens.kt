package com.linc.inphoto.ui

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.linc.inphoto.ui.auth.login.LoginFragment
import com.linc.inphoto.ui.splash.SplashFragment

object AppScreens {

    fun SplashScreen() = FragmentScreen {
        SplashFragment.newInstance()
    }

    fun LoginScreen() = FragmentScreen {
        LoginFragment.newInstance()
    }

}