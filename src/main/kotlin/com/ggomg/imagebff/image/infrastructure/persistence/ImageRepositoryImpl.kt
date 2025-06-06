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

    override fun read(id: UUID): Image {
        val entity = imageJpaRepository.findById(id)
            .orElseThrow { IllegalArgumentException("이미지 ID=$id 를 찾을 수 없습니다.") }
        return entity.toDomain()
    }
}