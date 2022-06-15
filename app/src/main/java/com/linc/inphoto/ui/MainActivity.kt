package com.linc.inphoto.ui

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji2.text.EmojiCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ActivityMainBinding
import com.linc.inphoto.di.navigation.GlobalNavigatorHolder
import com.linc.inphoto.ui.base.activity.LocaleActivity
import com.linc.inphoto.ui.localereload.LocaleReloadActivity
import com.linc.inphoto.ui.navigation.FragmentBackPressedListener
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.postDelayed
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.glide.GlideApp
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), LocaleActivity {

    companion object {
        private const val LOCALE_RELOAD_DELAY = 250L
    }

    private val localizationDelegate = LocalizationActivityDelegate(this)

    private val binding by viewBinding(ActivityMainBinding::bind)
    private val navigator = AppNavigator(this, R.id.globalContainerLayout)

    @GlobalNavigatorHolder
    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override val currentLocale: Locale get() = localizationDelegate.getLanguage(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        localizationDelegate.onCreate()
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

    override fun onResume() {
        super.onResume()
        localizationDelegate.onResume(this)
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

    override fun attachBaseContext(newBase: Context) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase))
        super.attachBaseContext(newBase)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(super.getResources())
    }

    override fun setLocale(locale: Locale) {
        postDelayed(LOCALE_RELOAD_DELAY) {
            localizationDelegate.setLanguage(this, locale)
        }
        startActivity(Intent(this, LocaleReloadActivity::class.java))
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}