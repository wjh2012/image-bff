package com.ggomg.imagebff.image.repository

import com.ggomg.imagebff.image.domain.Image
import com.ggomg.imagebff.image.domain.ImageRepository
import com.ggomg.imagebff.image.infrastructure.ImageEntity
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ImageRepositoryImpl(
    private val jpaImageRepository: JpaImageRepository
) : ImageRepository {

    override fun save(image: Image) {
        val entity = ImageEntity.fromDomain(image)
        jpaImageRepository.save(entity)
    }

    override fun saveAll(images: List<Image>) {
        val entities = images.map { ImageEntity.fromDomain(it) }
        jpaImageRepository.saveAll(entities)
    }

    override fun read(id: UUID): Image {
        val entity = jpaImageRepository.findById(id)
            .orElseThrow { IllegalArgumentException("이미지 ID=$id 를 찾을 수 없습니다.") }
        return entity.toDomain()
    }
}