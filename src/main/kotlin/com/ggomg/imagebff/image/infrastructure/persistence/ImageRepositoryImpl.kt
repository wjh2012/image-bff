package com.ggomg.imagebff.image.infrastructure.persistence

import com.ggomg.imagebff.image.domain.Image
import com.ggomg.imagebff.image.domain.ImageRepository
import com.ggomg.imagebff.image.infrastructure.persistence.entity.ImageEntity
import com.ggomg.imagebff.image.infrastructure.persistence.repository.ImageJpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ImageRepositoryImpl(
    private val imageJpaRepository: ImageJpaRepository
) : ImageRepository {

    override fun save(image: Image) {
        val entity = ImageEntity.Companion.fromDomain(image)
        imageJpaRepository.save(entity)
    }

    override fun saveAll(images: List<Image>) {
        val entities = images.map { ImageEntity.Companion.fromDomain(it) }
        imageJpaRepository.saveAll(entities)
    }

    override fun findById(id: UUID): Image? =
        imageJpaRepository.findById(id).map { it.toDomain() }.orElse(null)
}