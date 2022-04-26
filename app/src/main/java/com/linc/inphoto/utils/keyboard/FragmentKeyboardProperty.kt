package com.linc.inphoto.utils.keyboard

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentKeyboardProperty : ReadOnlyProperty<Fragment, KeyboardState> {

    internal var keyboardHelper: KeyboardHelper? = null
    private val lifecycleObserver = BindingLifecycleObserver()

    override fun getValue(thisRef: Fragment, property: KProperty<*>): KeyboardState {
        keyboardHelper?.let { return it }
        val view = thisRef.requireView()
        thisRef.viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        return KeyboardHelper().also {
            it.attach(view)
            keyboardHelper = it
        }
    }

    private inner class BindingLifecycleObserver : DefaultLifecycleObserver {
        @MainThread
        override fun onDestroy(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
            keyboardHelper?.detach()
            keyboardHelper = null
        }
    }

}