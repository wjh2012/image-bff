package com.ggomg.imagebff.image.model

data class UploadPresignedUrlRequest(
    val filenames: List<String>,
    val contentTypes: List<String>
)