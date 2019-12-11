package com.nott.extension

import com.flickr4java.flickr.photos.Size

val Size.subDirectoryName: String
    get() = when (this.label) {
        Size.THUMB -> "ex_thumb"
        Size.SQUARE -> "ex_square"
        Size.SMALL -> "ex_small"
        Size.MEDIUM -> "ex_medium"
        Size.LARGE -> "ex_large"
        Size.ORIGINAL -> "ex_original"
        Size.SQUARE_LARGE -> "ex_square_large"
        Size.SMALL_320 -> "ex_small_320"
        Size.MEDIUM_640 -> "ex_medium_640"
        Size.MEDIUM_800 -> "ex_medium_800"
        else -> "ex_other"
    }