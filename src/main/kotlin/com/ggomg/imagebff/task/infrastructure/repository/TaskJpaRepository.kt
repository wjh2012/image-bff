package com.ggomg.imagebff.task.infrastructure.repository

import com.ggomg.imagebff.task.infrastructure.entity.TaskEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TaskJpaRepository : JpaRepository<TaskEntity, UUID> {

    fun findByUserIdAndIdNull(userId: UUID, id: UUID): TaskEntity?
}