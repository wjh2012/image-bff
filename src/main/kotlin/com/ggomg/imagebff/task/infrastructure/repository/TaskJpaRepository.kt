package com.ggomg.imagebff.task.infrastructure.repository

import com.ggomg.imagebff.task.infrastructure.entity.TaskEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface TaskJpaRepository : JpaRepository<TaskEntity, UUID> {

    fun findAllByUserId(userId: UUID): List<TaskEntity>
    fun findByUserIdAndId(userId: UUID, id: UUID): TaskEntity?

    // (2) 비관적 락을 걸어주는 메서드
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
        """
      SELECT t 
      FROM TaskEntity t 
      WHERE t.userId = :userId 
        AND t.id     = :id
    """
    )
    fun reserveTaskForProcessing(
        @Param("userId") userId: UUID,
        @Param("id") id: UUID
    ): TaskEntity?
}