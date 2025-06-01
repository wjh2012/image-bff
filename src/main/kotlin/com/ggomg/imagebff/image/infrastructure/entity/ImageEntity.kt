package com.ggomg.imagebff.image.infrastructure.entity

import com.ggomg.imagebff.common.base.BaseEntity
import com.ggomg.imagebff.image.domain.Image
import com.ggomg.imagebff.image.domain.UploadStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "images")
class ImageEntity(
    @Id
    val id: UUID,
    @Column
    val userId: UUID,

    @Column
    val imageCreatedAt: LocalDateTime?,

    @Column(nullable = false)
    val filename: String,
    @Column(nullable = false)
    val contentType: String,

    @Column
    val uploadedAt: LocalDateTime?,
    @Column
    val uploadStatus: UploadStatus = UploadStatus.PENDING,
) : BaseEntity() {

    fun toDomain(): Image =
        Image(imageId = id.toString(), userId = userId.toString(), filename = filename, contentType = contentType)

    companion object {
        fun fromDomain(image: Image): ImageEntity =
            ImageEntity(
                id = UUID.fromString(image.imageId),
                userId = UUID.fromString(image.imageId),
                imageCreatedAt = image.imageCreatedAt,
                filename = image.filename,
                contentType = image.contentType,
                uploadedAt = image.uploadedAt,
                uploadStatus = image.uploadStatus
            )
    }
}