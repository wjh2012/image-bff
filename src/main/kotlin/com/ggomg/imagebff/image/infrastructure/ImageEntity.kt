package com.ggomg.imagebff.image.infrastructure

import com.ggomg.imagebff.common.base.BaseEntity
import com.ggomg.imagebff.image.domain.Image
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "images")
class ImageEntity(
    @Id
    val id: UUID,

    @Column(nullable = false)
    val filename: String,

    @Column(nullable = false)
    val contentType: String,

    @Column
    val uploaded: Boolean = false,
) : BaseEntity() {

    fun toDomain(): Image = Image(id, filename, contentType, uploaded)

    companion object {
        fun fromDomain(image: Image): ImageEntity =
            ImageEntity(image.imageId, image.originalName, image.contentType, image.uploadStatus)
    }
}