package com.linc.inphoto.utils.extensions

import androidx.viewbinding.ViewBinding
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem

fun createAdapter(vararg sections: Section): GroupieAdapter =
    createAdapter(hasStableIds = true, *sections)

@JvmOverloads
fun createAdapter(
    hasStableIds: Boolean,
    vararg sections: Section
) = GroupieAdapter().apply {
    addAll(sections.toList())
    setHasStableIds(hasStableIds)
}

fun <T : BindableItem<out ViewBinding>> Section.updateSingle(item: T) {
    update(listOf(item))
}

fun <T : BindableItem<out ViewBinding>> Section.update(
    items: List<T>,
    onNewItemAdded: () -> Unit
) {
    val oldSize = groups.count()
    update(items)
    if (items.count() > oldSize) {
        onNewItemAdded()
    }
}