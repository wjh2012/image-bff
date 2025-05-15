package com.ggomg.imagebff.image.repository

import com.ggomg.imagebff.image.infrastructure.ImageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface JpaImageRepository : JpaRepository<ImageEntity, UUID> {
}