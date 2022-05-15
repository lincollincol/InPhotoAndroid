package com.linc.inphoto.utils

import com.google.firebase.firestore.DocumentSnapshot
import com.linc.inphoto.utils.extensions.safeCast

//fun <T : Any> QueryDocumentSnapshot.getList(field: String) =
//    get(field)?.safeCast<List<T>>().orEmpty()

fun <T : Any> DocumentSnapshot.getList(field: String) =
    get(field)?.safeCast<List<T>>().orEmpty()

fun DocumentSnapshot.getTimestampMillis(
    field: String,
    default: Long = 0L
) = getTimestamp(field)?.toDate()?.time ?: default