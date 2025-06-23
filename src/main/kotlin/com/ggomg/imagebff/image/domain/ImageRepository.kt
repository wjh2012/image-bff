package com.ggomg.imagebff.image.domain

import java.util.UUID

interface ImageRepository {
    fun save(image: Image)
    fun saveAll(images: List<Image>)
    fun findById(id: UUID): Image?
    fun findAllByIds(ids: List<UUID>): List<Image>
}