package com.ggomg.imagebff.task.infrastructure

import com.ggomg.imagebff.task.application.TaskLockingRepository
import com.ggomg.imagebff.task.domain.Task
import com.ggomg.imagebff.task.domain.TaskRepository
import com.ggomg.imagebff.task.infrastructure.repository.TaskJpaRepository
import com.ggomg.imagebff.task.utils.TaskMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class TaskRepositoryImpl(
    private val taskJpaRepository: TaskJpaRepository,
) : TaskRepository, TaskLockingRepository {

    override fun findById(id: UUID): Task? {
        val taskEntity = taskJpaRepository.findByIdOrNull(id)
        return taskEntity?.let { TaskMapper.toDomain(taskEntity) }
    }

    override fun findAllByUserId(userId: UUID): List<Task> {
        val taskEntities = taskJpaRepository.findAllByUserId(userId)
        return taskEntities.map { TaskMapper.toDomain(it) }
    }

    override fun findByUserIdAndId(
        userId: UUID,
        id: UUID
    ): Task? {
        val taskEntity = taskJpaRepository.findByUserIdAndId(userId, id)
        return taskEntity?.let { TaskMapper.toDomain(taskEntity) }
    }

    override fun save(task: Task): Task {
        val taskEntity = TaskMapper.toEntity(task)
        val savedTaskEntity = taskJpaRepository.save(taskEntity)
        return TaskMapper.toDomain(savedTaskEntity)
    }

    override fun delete(task: Task) {
        val taskEntity = TaskMapper.toEntity(task)
        taskJpaRepository.delete(taskEntity)
    }

    override fun findByUserIdAndIdForUpdate(
        userId: UUID,
        id: UUID
    ): Task? {
        return taskJpaRepository.reserveTaskForProcessing(userId, id)
            ?.let { TaskMapper.toDomain(it) }
    }
}