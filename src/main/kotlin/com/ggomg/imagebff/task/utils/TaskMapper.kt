package com.ggomg.imagebff.task.utils

import com.ggomg.imagebff.task.domain.Task
import com.ggomg.imagebff.task.domain.mapStateToStatus
import com.ggomg.imagebff.task.domain.mapStatusToState
import com.ggomg.imagebff.task.infrastructure.entity.TaskEntity

object TaskMapper {
    fun toDomain(entity: TaskEntity): Task =
        Task(
            id = entity.id,
            userId = entity.userId,
            name = entity.name,
            status = mapStatusToState(entity.status),
            createdAt = entity.taskCreatedAt,
            updatedAt = entity.taskModifiedAt
        )

    fun toEntity(task: Task): TaskEntity =
        TaskEntity(
            id = task.getId(),
            userId = task.getUserId(),
            name = task.getName(),
            status = mapStateToStatus(task.getStatus()),
            taskCreatedAt = task.getCreatedAt(),
            taskModifiedAt = task.getUpdatedAt()
        )
}