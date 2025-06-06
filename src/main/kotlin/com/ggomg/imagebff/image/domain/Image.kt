package com.ggomg.imagebff.image.domain

import com.ggomg.imagebff.image.utils.ContentTypeUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Image(
    val id: UUID,
    val userId: UUID,

    val imageCreatedAt: LocalDateTime = LocalDateTime.now(),

    val filename: String,
    val contentType: String,

    var uploadedAt: LocalDateTime? = null,
    var uploadStatus: UploadStatus = UploadStatus.PENDING,
) {
    fun markUploaded() {
        this.uploadStatus = UploadStatus.SUCCESS
        this.uploadedAt = LocalDateTime.now()
    }

    fun generateObjectKey(): String {
        val shortUuid = id.toString().replace("-", "").takeLast(8)
        val datePath = imageCreatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        val timePart = imageCreatedAt.format(DateTimeFormatter.ofPattern("HHmmss"))
        val prefix = "original"
        val ext = ContentTypeUtil.getExtension(contentType)
        return "$datePath/$prefix/${timePart}_$shortUuid.$ext"
    }

}
