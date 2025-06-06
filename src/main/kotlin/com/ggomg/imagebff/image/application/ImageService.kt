package com.ggomg.imagebff.image.application

import com.fasterxml.uuid.Generators
import com.ggomg.imagebff.image.domain.Image
import com.ggomg.imagebff.image.domain.ImageRepository
import com.ggomg.imagebff.image.domain.ImageStorage
import com.ggomg.imagebff.image.model.PresignedUploadUrl
import com.ggomg.imagebff.user.domain.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class ImageService(
    private val imageStorage: ImageStorage,
    private val imageRepository: ImageRepository,
    private val userRepository: UserRepository
) {

    fun save(userEmail: String, filename: String, contentType: String): PresignedUploadUrl {
        val generatedUUID = Generators.timeBasedEpochGenerator().generate()
        val createdAt = LocalDateTime.now()

        val user = userRepository.findByEmail(userEmail) ?: throw IllegalArgumentException("User not found.")

        val image = Image(
            id = generatedUUID,
            userId = user.id,
            imageCreatedAt = createdAt,
            filename = filename,
            contentType = contentType
        )
        val presignedUrl = imageStorage.generateUploadPresignedUrl(image)
        imageRepository.save(image)
        return presignedUrl
    }

    fun saveAll(userEmail: String, filenames: List<String>, contentTypes: List<String>): List<PresignedUploadUrl> {
        return filenames.zip(contentTypes).map { (filename, contentType) ->
            val imageId = Generators.timeBasedEpochGenerator().generate()
            val createdAt = LocalDateTime.now()

            val user = userRepository.findByEmail(userEmail) ?: throw IllegalArgumentException("User not found.")

            val image = Image(
                id = imageId,
                userId = user.id,
                imageCreatedAt = createdAt,
                filename = filename,
                contentType = contentType
            )
            val presignedUrl = imageStorage.generateUploadPresignedUrl(image)
            imageRepository.save(image)
            presignedUrl
        }
    }

    fun read(userId: String, imageId: UUID): PresignedUploadUrl {
        val image = imageRepository.read(imageId)
        val presignedUrl = imageStorage.generateDownloadPresignedUrl(image)
        return presignedUrl
    }

    fun readAll(userId: String, imageIds: List<UUID>): List<PresignedUploadUrl> {
        return imageIds.map { id ->
            val image = imageRepository.read(id)
            val presignedUrl = imageStorage.generateDownloadPresignedUrl(image)
            presignedUrl
        }
    }

    fun confirmUpload(userId: String, imageId: UUID) {
        val image = imageRepository.read(imageId)
        image.markUploaded()
        imageRepository.save(image)
    }

    fun confirmAllUploads(userId: String, imageIds: List<UUID>) {
        val images = imageIds.map { id ->
            val image = imageRepository.read(id)
            image.markUploaded()
            image
        }
        imageRepository.saveAll(images)
    }
}
