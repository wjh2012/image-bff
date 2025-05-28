package com.ggomg.imagebff.image.domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Image(
    val imageId: UUID,
    val originalName: String,
    val contentType: String,
    var uploadStatus: Boolean,
) {

    fun markUploaded() {
        this.uploadStatus = true
    }

    fun generateObjectKey(): String {
        val shortUuid = imageId.toString().replace("-", "").takeLast(8)

        val now = LocalDateTime.now()
        val datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        val timePart = now.format(DateTimeFormatter.ofPattern("HHmmss"))

        val prefix = "original"  // 필요시 타입별로 "thumbnail", "resized" 등도 가능
        val ext = getExtensionFromContentType(contentType)

        return "$datePath/$prefix/${timePart}_$shortUuid.$ext"
    }

    private fun getExtensionFromContentType(contentType: String): String {
        return when (contentType.lowercase()) {
            "image/jpeg", "image/jpg" -> "jpg"
            "image/png" -> "png"
            "image/gif" -> "gif"
            else -> "bin"
        }
    }
}
