package com.ggomg.imagebff.image.domain

import java.util.UUID

data class Image(
    val imageId: UUID,
    val filename: String,
    val contentType: String,
    var uploadStatus: Boolean,
) {
    fun getObjectKey(): String {
        val extension = filename.substringAfterLast('.', "bin")
        return "images/$imageId.$extension"
    }

    fun markUploaded() {
        this.uploadStatus = true
    }
}