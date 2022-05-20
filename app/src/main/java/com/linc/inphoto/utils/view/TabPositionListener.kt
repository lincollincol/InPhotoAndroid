package com.linc.inphoto.utils.view

import com.google.android.material.tabs.TabLayout

class TabPositionListener(
    private val onTabSelectedAt: (Int) -> Unit
) : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab) {
        onTabSelectedAt(tab.position)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        /** Not Implemented */
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        /** Not Implemented */
    }
}