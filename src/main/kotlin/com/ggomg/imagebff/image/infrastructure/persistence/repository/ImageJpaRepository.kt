package com.ggomg.imagebff.image.infrastructure.persistence.repository

import com.ggomg.imagebff.image.infrastructure.persistence.entity.ImageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ImageJpaRepository : JpaRepository<ImageEntity, UUID> {
    fun findByUserIdAndId(userId: UUID, id: UUID): ImageEntity?
    fun findAllByUserIdAndIdIn(userId: UUID, ids: List<UUID>): List<ImageEntity>
}