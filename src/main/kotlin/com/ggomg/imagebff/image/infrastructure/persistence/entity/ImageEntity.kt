package com.ggomg.imagebff.image.infrastructure.persistence.entity

import com.ggomg.imagebff.common.base.BaseEntity
import com.ggomg.imagebff.image.domain.Image
import com.ggomg.imagebff.image.domain.UploadStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
    @Enumerated(EnumType.STRING)
    val uploadStatus: UploadStatus = UploadStatus.PENDING,
) : BaseEntity() {

    fun toDomain(): Image =
        Image(id = id, userId = userId, filename = filename, contentType = contentType)

    companion object {
        fun fromDomain(image: Image): ImageEntity =
            ImageEntity(
                id = image.id,
                userId = image.id,
                imageCreatedAt = image.imageCreatedAt,
                filename = image.filename,
                contentType = image.contentType,
                uploadedAt = image.uploadedAt,
                uploadStatus = image.uploadStatus
            )
    }
}