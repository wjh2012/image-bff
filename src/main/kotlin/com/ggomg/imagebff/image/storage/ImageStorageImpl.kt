package com.ggomg.imagebff.image.storage

import com.ggomg.imagebff.config.MinioProperties
import com.ggomg.imagebff.image.domain.Image
import com.ggomg.imagebff.image.domain.ImageStorage
import com.ggomg.imagebff.image.model.PresignedUploadUrl
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import org.springframework.stereotype.Component

@Component
class ImageStorageImpl(
    private val minioClient: MinioClient,
    private val minioProperties: MinioProperties
) : ImageStorage {
    override fun generateUploadPresignedUrl(
        image: Image
    ): PresignedUploadUrl {
        val url = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(minioProperties.bucket)
                .`object`(image.getObjectKey())
                .expiry(60 * 10)
                .build()
        )

        return PresignedUploadUrl(image.imageId, url)
    }

    override fun generateDownloadPresignedUrl(
        image: Image
    ): PresignedUploadUrl {
        val url = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(minioProperties.bucket)
                .`object`(image.getObjectKey())
                .expiry(60 * 10)
                .build()
        )

        return PresignedUploadUrl(image.imageId, url)
    }
}