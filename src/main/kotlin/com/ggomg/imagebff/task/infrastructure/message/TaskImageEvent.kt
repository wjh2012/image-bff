package com.ggomg.imagebff.task.infrastructure.message

import java.io.Serializable
import java.util.UUID

data class TaskImageEvent(
    val taskId: UUID,
    val userId: UUID,
    val occurredAt: String = java.time.Instant.now().toString()
)
