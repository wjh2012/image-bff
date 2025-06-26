package com.ggomg.imagebff.image.infrastructure.persistence

import com.ggomg.imagebff.image.domain.Image
import com.ggomg.imagebff.image.domain.ImageRepository
import com.ggomg.imagebff.image.infrastructure.persistence.entity.ImageEntity
import com.ggomg.imagebff.image.infrastructure.persistence.repository.ImageJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ImageRepositoryImpl(
    private val imageJpaRepository: ImageJpaRepository
) : ImageRepository {

    override fun save(image: Image) {
        val entity = ImageEntity.fromDomain(image)
        imageJpaRepository.save(entity)
    }

    override fun saveAll(images: List<Image>) {
        val entities = images.map { ImageEntity.fromDomain(it) }
        imageJpaRepository.saveAll(entities)
    }

    override fun findById(id: UUID): Image? =
        imageJpaRepository.findByIdOrNull(id)?.toDomain()

    override fun findAllByIds(ids: List<UUID>) =
        imageJpaRepository.findAllById(ids).map { it.toDomain() }

    override fun findByUserIdAndId(
        userId: UUID,
        id: UUID
    ): Image? {
        return imageJpaRepository.findByUserIdAndId(userId, id)?.toDomain()
    }

    override fun findAllByUserIdAndIdIn(
        userId: UUID,
        ids: List<UUID>
    ): List<Image> {
        return imageJpaRepository.findAllByUserIdAndIdIn(userId, ids).map { it.toDomain() }
    }
}