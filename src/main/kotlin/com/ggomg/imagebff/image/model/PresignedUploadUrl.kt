package com.ggomg.imagebff.image.model

import java.util.UUID

data class PresignedUploadUrl(
    val imageId: UUID,
    val presignedUrl: String
)
