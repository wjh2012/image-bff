package com.ggomg.imagebff.task.infrastructure

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
    private val taskMapper: TaskMapper
) : TaskRepository {

    override fun findById(id: UUID): Task? {
        val taskEntity = taskJpaRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("User not found.")
        return taskMapper.toDomain(taskEntity)
    }

    override fun save(task: Task): Task {
        val taskEntity = taskMapper.toEntity(task)
        val savedTaskEntity = taskJpaRepository.save(taskEntity)
        return taskMapper.toDomain(savedTaskEntity)
    }

    override fun delete(task: Task) {
        val taskEntity = taskMapper.toEntity(task)
        taskJpaRepository.delete(taskEntity)
    }
}