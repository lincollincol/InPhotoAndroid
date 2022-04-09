package com.linc.inphoto.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.NavigatorHolder
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ActivityMainBinding
import com.linc.inphoto.ui.navigation.MultiTabNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val navigator = MultiTabNavigator(this, R.id.fragmentContainerLayout)
    private val binding by viewBinding(ActivityMainBinding::bind)

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            navigator.initNavigator(MenuTab.HOME)
        }
        binding.bottomNavigationView.onTabSelected = { tab ->
            MenuTab.fromId(tab.id)?.let(navigator::selectTab)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Glide.get(this).clearMemory()
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
        navigator.handleBackPressed { tab ->
            binding.bottomNavigationView.selectTabById(tab.id)
        }
    }

}