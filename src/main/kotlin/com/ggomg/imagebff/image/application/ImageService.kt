package com.ggomg.imagebff.image.application

import com.ggomg.imagebff.image.model.PresignedUploadUrl
import java.util.UUID


interface ImageService {
    fun save(filename: String, contentType: String): PresignedUploadUrl
    fun saveAll(filenames: List<String>, contentTypes: List<String>): List<PresignedUploadUrl>

    fun read(imageId: UUID): PresignedUploadUrl
    fun readAll(imageIds: List<UUID>): List<PresignedUploadUrl>

    fun confirmUpload(imageId: UUID)
    fun confirmAllUploads(imageIds: List<UUID>)
}