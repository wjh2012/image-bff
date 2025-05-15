package com.ggomg.imagebff.image.domain

import com.ggomg.imagebff.image.model.PresignedUploadUrl

interface ImageStorage {
    fun generateUploadPresignedUrl(image: Image): PresignedUploadUrl
    fun generateDownloadPresignedUrl(image: Image): PresignedUploadUrl
}