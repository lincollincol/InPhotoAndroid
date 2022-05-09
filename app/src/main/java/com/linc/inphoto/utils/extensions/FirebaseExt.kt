package com.linc.inphoto.utils

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.linc.inphoto.utils.extensions.safeCast

fun <T : Any> QueryDocumentSnapshot.getList(field: String) =
    get(field)?.safeCast<List<T>>().orEmpty()

fun <T : Any> DocumentSnapshot.getList(field: String) =
    get(field)?.safeCast<List<T>>().orEmpty()