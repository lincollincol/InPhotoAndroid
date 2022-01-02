package com.linc.inphoto.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.linc.inphoto.R
import com.linc.inphoto.ui.navigation.AppScreens
import com.linc.inphoto.utils.FragmentBackPressedListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navigator = AppNavigator(this, R.id.fragment_container)

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            navigator.applyCommands(arrayOf(Replace(AppScreens.SplashScreen())))
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        fragment?.let {
            (it as? FragmentBackPressedListener)?.onBackPressed()
        } ?: super.onBackPressed()
    }

}

/**
 * TODO:
 * - auth
 * - profile
 * - base vm
 * - base widgets (shapes, ripples etc)
 *
 * */