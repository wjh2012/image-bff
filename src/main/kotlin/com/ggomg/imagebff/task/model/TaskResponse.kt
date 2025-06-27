package com.ggomg.imagebff.task.model

import java.time.LocalDateTime


data class TaskResponse(
    val id: String,
    val name: String,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val images: List<TaskImageResponse>
)
