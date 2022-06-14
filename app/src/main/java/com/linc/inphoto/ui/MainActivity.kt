package com.linc.inphoto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji2.text.EmojiCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ActivityMainBinding
import com.linc.inphoto.di.navigation.GlobalNavigatorHolder
import com.linc.inphoto.ui.navigation.FragmentBackPressedListener
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.glide.GlideApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)
    private val navigator = AppNavigator(this, R.id.globalContainerLayout)

    @GlobalNavigatorHolder
    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiCompat.init(this)

        if (savedInstanceState == null) {
            navigator.applyCommands(arrayOf(Replace(NavScreen.SplashScreen())))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Glide.get(applicationContext).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        GlideApp.with(applicationContext).onTrimMemory(TRIM_MEMORY_MODERATE)
        super.onTrimMemory(level)
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
        supportFragmentManager.fragments
            .firstOrNull { it.isVisible }
            ?.safeCast<FragmentBackPressedListener>()
            ?.onBackPressed()
    }

}