package com.linc.inphoto.utils.extensions

import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section


fun createAdapter(vararg sections: Section): GroupieAdapter {
    return GroupieAdapter().apply { addAll(sections.toList()) }
}