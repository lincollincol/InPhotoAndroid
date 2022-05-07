package com.linc.inphoto.utils.extensions.view

import com.google.android.material.tabs.TabLayout

fun TabLayout.setTabTitle(index: Int, title: String) {
    getTabAt(index)?.text = title
}