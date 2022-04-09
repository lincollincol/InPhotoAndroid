package com.linc.inphoto.ui.navigation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.Creator

interface DialogScreen : Screen {
    val clearContainer: Boolean get() = true
    fun createDialog(factory: FragmentFactory): DialogFragment

    companion object {
        operator fun invoke(
            key: String? = null,
            clearContainer: Boolean = true,
            fragmentCreator: Creator<FragmentFactory, DialogFragment>
        ) = object : DialogScreen {
            override val screenKey = key ?: fragmentCreator::class.java.name
            override val clearContainer = clearContainer
            override fun createDialog(factory: FragmentFactory) = fragmentCreator.create(factory)
        }
    }
}


/*
package com.linc.inphoto.ui.navigation

import android.os.Parcelable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.Creator
import com.github.terrakok.cicerone.androidx.FragmentScreen

open class DialogScreen constructor(
    val key: String? = null,
//    override val screenKey: String = fragmentCreator::class.java.name,
    override val clearContainer: Boolean = true,
    private val dialogCreator: Creator<FragmentFactory, DialogFragment>
) : FragmentScreen {
    override val screenKey: String = key ?:

//    companion object {
//        operator fun invoke(
//            key: String? = null,
//            clearContainer: Boolean = true,
//            fragmentCreator: Creator<FragmentFactory, Fragment>
//        ) = object : FragmentScreen {
//            override val screenKey = key ?: fragmentCreator::class.java.name
//            override val clearContainer = clearContainer
//            override fun createFragment(factory: FragmentFactory) = fragmentCreator.create(factory)
//        }
//    }

    override fun createFragment(factory: FragmentFactory): Fragment {
        return dialogCreator.create(factory)
    }
}
 */