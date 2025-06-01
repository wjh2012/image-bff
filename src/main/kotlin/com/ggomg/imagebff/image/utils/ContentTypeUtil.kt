package com.ggomg.imagebff.image.utils

object ContentTypeUtil {
    fun getExtension(contentType: String): String {
        return when (contentType.lowercase()) {
            "image/jpeg", "image/jpg" -> "jpg"
            "image/png" -> "png"
            "image/gif" -> "gif"
            else -> "bin"
        }
    }
}