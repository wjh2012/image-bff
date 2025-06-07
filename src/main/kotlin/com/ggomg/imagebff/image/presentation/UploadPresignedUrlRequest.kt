package com.ggomg.imagebff.image.presentation

data class UploadPresignedUrlRequest(
    val filenames: List<String>,
    val contentTypes: List<String>
)
