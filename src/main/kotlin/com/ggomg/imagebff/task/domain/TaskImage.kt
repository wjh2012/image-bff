package com.ggomg.imagebff.task.domain

import java.util.UUID

class TaskImage(
    val id: UUID,
    val taskId: UUID,
    val imageId: UUID,
) {
}