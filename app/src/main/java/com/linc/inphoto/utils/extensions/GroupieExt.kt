package com.linc.inphoto.utils.extensions

import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section

fun createAdapter(
    hasStableIds: Boolean = true,
    vararg sections: Section
): GroupieAdapter {
    return GroupieAdapter().apply {
        addAll(sections.toList())
        setHasStableIds(hasStableIds)
    }
}