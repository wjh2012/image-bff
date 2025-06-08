package com.ggomg.imagebff.task.utils

import com.ggomg.imagebff.task.domain.Task
import com.ggomg.imagebff.task.infrastructure.entity.TaskEntity

object TaskMapper {
    fun toDomain(entity: TaskEntity): Task =
        Task(
            id = entity.id,
            userId = entity.userId,
            name = entity.name,
            createdAt = entity.taskCreatedAt,
            updatedAt = entity.taskModifiedAt
        )

    fun toEntity(task: Task): TaskEntity =
        TaskEntity(
            id = task.id,
            userId = task.userId,
            name = task.name,
            taskCreatedAt = task.createdAt,
            taskModifiedAt = task.updatedAt
        )
}