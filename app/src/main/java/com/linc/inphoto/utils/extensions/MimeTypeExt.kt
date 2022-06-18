package com.linc.inphoto.utils.extensions

const val MIME_AUDIO_PREFIX = "audio"
const val MIME_APPLICATION_PREFIX = "application"
const val MIME_FONT_PREFIX = "font"
const val MIME_IMAGE_PREFIX = "image"
const val MIME_TEXT_PREFIX = "text"
const val MIME_VIDEO_PREFIX = "video"

fun String?.isAudioMimeType() = isMime(MIME_AUDIO_PREFIX)
fun String?.isVideoMimeType() = isMime(MIME_VIDEO_PREFIX)
fun String?.isImageMimeType() = isMime(MIME_IMAGE_PREFIX)
fun String?.isFontMimeType() = isMime(MIME_FONT_PREFIX)
fun String?.isDocMimeType() = isMime(MIME_APPLICATION_PREFIX, MIME_TEXT_PREFIX)

private fun String?.isMime(vararg prefix: String) =
    this?.let { mime -> prefix.any { mime.startsWith(it, ignoreCase = true) } } ?: false