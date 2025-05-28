package com.ggomg.imagebff.image.storage

import com.ggomg.imagebff.config.MinioProperties
import com.ggomg.imagebff.image.domain.Image
import com.ggomg.imagebff.image.domain.ImageStorage
import com.ggomg.imagebff.image.model.PresignedUploadUrl
import io.github.oshai.kotlinlogging.KotlinLogging
import io.minio.BucketExistsArgs
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.http.Method
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class ImageStorageImpl(
    private val minioClient: MinioClient,
    private val minioProperties: MinioProperties
) : ImageStorage {

    @PostConstruct
    fun ensureBucketExists() {
        val bucketName = minioProperties.bucket
        try {
            val exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
            )
            if (!exists) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
                )
                logger.info { "✅ 버킷 생성 완료: $bucketName" }
            } else {
                logger.info { "ℹ️ 이미 존재하는 버킷: $bucketName" }
            }
        } catch (e: Exception) {
            throw RuntimeException("MinIO 버킷 확인/생성 실패", e)
        }
    }

    override fun generateUploadPresignedUrl(
        image: Image
    ): PresignedUploadUrl {
        val url = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(minioProperties.bucket)
                .`object`(image.generateObjectKey())
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
                .`object`(image.generateObjectKey())
                .expiry(60 * 10)
                .build()
        )

        return PresignedUploadUrl(image.imageId, url)
    }
}