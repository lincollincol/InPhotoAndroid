package com.linc.inphoto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.NavigatorHolder
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ActivityMainBinding
import com.linc.inphoto.ui.navigation.FragmentBackPressedListener
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
//        setContentView(R.layout.activity_main)

//        if (savedInstanceState == null) {
//            navigator.applyCommands(arrayOf(Replace(Navigation.SplashScreen())))
//        }
//        tabsHolder.initTabs(listOf("Home", "Feed", "Chats", "Profile"))
//        val menu = findViewById<BottomNavigationView>(R.id.bottom_menu)
//        menu.setOnItemSelectedListener {
//            val container = when(it.itemId) {
//                R.id.homeMenuItem -> "Home"
//                R.id.feedMenuItem -> "Feed"
//                R.id.chatMenuItem -> "Chat"
//                else -> "Profile"
//            }
//            navigator.selectTab(container)
//            return@setOnItemSelectedListener true
//        }

        /*
                "Profile" -> ProfileScreen()
        "Home" -> SignUpScreen()
        "Chats" -> SignInScreen()
        "Feed" -> SplashScreen()
         */
        binding.bottomNavigationView.onTabSelected = { tab ->
            val tabId = when (tab.id) {
                R.id.homeMenuItem -> "Home"
                R.id.feedMenuItem -> "Feed"
                R.id.chatMenuItem -> "Chats"
                else -> "Profile"
            }
            navigator.selectTab(tabId)
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

    //    override fun onBackPressed() {
//        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerLayout)
//        fragment?.let {
//            (it as? FragmentBackPressedListener)?.onBackPressed()
//        } ?: super.onBackPressed()
//    }
    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        fragments.firstOrNull { it.isVisible }?.let {
            (it as? FragmentBackPressedListener)?.onBackPressed()
        } ?: super.onBackPressed()
    }

}