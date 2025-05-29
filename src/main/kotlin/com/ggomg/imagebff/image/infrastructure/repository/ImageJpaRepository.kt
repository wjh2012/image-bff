package com.ggomg.imagebff.image.infrastructure.repository

import com.ggomg.imagebff.image.infrastructure.entity.ImageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ImageJpaRepository : JpaRepository<ImageEntity, UUID> {
}