package com.linc.inphoto.utils.extensions.view

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

fun TabLayout.setTabTitle(index: Int, title: String) {
    getTabAt(index)?.text = title
}

fun TabLayout.selectTab(index: Int) {
    if (selectedTabPosition != index)
        getTabAt(index)?.select()
}

fun TabLayout.attachMediator(
    viewPager: ViewPager2,
    tabConfiguration: (tab: TabLayout.Tab, position: Int) -> Unit
) = TabLayoutMediator(this, viewPager, tabConfiguration).attach()

fun ViewPager2.selectPage(index: Int) {
    if (currentItem != index)
        setCurrentItem(index, false)
}