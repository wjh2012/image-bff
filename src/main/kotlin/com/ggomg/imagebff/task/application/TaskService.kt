package com.ggomg.imagebff.task.application

import com.fasterxml.uuid.Generators
import com.ggomg.imagebff.task.domain.Task
import com.ggomg.imagebff.task.domain.TaskRepository
import com.ggomg.imagebff.user.domain.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) {
    fun save(userId: UUID, taskName: String) {
        userRepository.findById(userId) ?: throw IllegalArgumentException("User not found.")

        val generatedUUID = Generators.timeBasedEpochGenerator().generate()
        val createdAt = LocalDateTime.now()

        val task = Task(
            id = generatedUUID,
            userId = userId,
            name = taskName,
            createdAt = createdAt,
            updatedAt = createdAt,
        )

        taskRepository.save(task)
    }

    fun changeTaskName(userId: UUID, taskId: UUID, newName: String) {
        userRepository.findById(userId) ?: throw IllegalArgumentException("User not found.")

        val task =
            taskRepository.findById(taskId) ?: throw IllegalArgumentException("Task not found.")
        task.changeName(newName)
        taskRepository.save(task)
    }

    fun delete(userId: UUID, taskId: UUID) {
        userRepository.findById(userId) ?: throw IllegalArgumentException("User not found.")

        val task =
            taskRepository.findById(taskId) ?: throw IllegalArgumentException("Task not found.")
        taskRepository.delete(task)
    }

}