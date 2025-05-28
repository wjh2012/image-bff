package com.ggomg.imagebff.image.application

import com.fasterxml.uuid.Generators
import com.ggomg.imagebff.image.domain.Image
import com.ggomg.imagebff.image.domain.ImageRepository
import com.ggomg.imagebff.image.domain.ImageStorage
import com.ggomg.imagebff.image.model.PresignedUploadUrl
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ImageServiceImpl(
    private val imageStorage: ImageStorage,
    private val imageRepository: ImageRepository,
) : ImageService {

    override fun save(filename: String, contentType: String): PresignedUploadUrl {
        val imageId = Generators.timeBasedEpochGenerator().generate()
        val image = Image(imageId, filename, contentType, false)
        val presignedUrl = imageStorage.generateUploadPresignedUrl(image)
        imageRepository.save(image)
        return presignedUrl
    }

    override fun saveAll(filenames: List<String>, contentTypes: List<String>): List<PresignedUploadUrl> {
        return filenames.zip(contentTypes).map { (filename, contentType) ->
            val imageId = Generators.timeBasedEpochGenerator().generate()
            val image = Image(
                imageId = imageId,
                originalName = filename,
                contentType = contentType,
                uploadStatus = false,
            )
            val presignedUrl = imageStorage.generateUploadPresignedUrl(image)
            imageRepository.save(image)
            presignedUrl
        }
    }

    override fun read(imageId: UUID): PresignedUploadUrl {
        val image = imageRepository.read(imageId)
        val presignedUrl = imageStorage.generateDownloadPresignedUrl(image)
        return presignedUrl
    }

    override fun readAll(imageIds: List<UUID>): List<PresignedUploadUrl> {
        return imageIds.map { id ->
            val image = imageRepository.read(id)
            val presignedUrl = imageStorage.generateDownloadPresignedUrl(image)
            presignedUrl
        }
    }

    override fun confirmUpload(imageId: UUID) {
        val image = imageRepository.read(imageId)
        image.markUploaded()
        imageRepository.save(image)
    }

    override fun confirmAllUploads(imageIds: List<UUID>) {
        val images = imageIds.map { id ->
            val image = imageRepository.read(id)
            image.markUploaded()
            image
        }
        imageRepository.saveAll(images)
    }
}
