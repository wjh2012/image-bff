package com.ggomg.imagebff.image.domain

import java.util.UUID

interface ImageRepository {
    fun save(image: Image)
    fun saveAll(images: List<Image>)
    fun read(id: UUID): Image
}