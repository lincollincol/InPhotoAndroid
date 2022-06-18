package com.linc.inphoto.data.android.model

import android.net.Uri
import java.io.File

@Deprecated("Future release feature")
sealed class MediaFile(
    val source: File,
    val type: String
) {

    val uri: Uri get() = Uri.fromFile(source)

    class Image(file: File, type: String) : MediaFile(file, type)

    class Video(file: File, type: String) : MediaFile(file, type)

    class Audio(
        file: File,
        type: String,
        val waveform: List<Int>
    ) : MediaFile(file, type)

    class Document(file: File, type: String) : MediaFile(file, type)

}