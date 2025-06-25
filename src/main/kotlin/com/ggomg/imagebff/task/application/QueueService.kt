package com.ggomg.imagebff.task.application

import BusinessException
import com.ggomg.imagebff.image.domain.ImageRepository
import com.ggomg.imagebff.task.domain.TaskRepository
import com.ggomg.imagebff.task.exception.TaskErrorCode
import com.ggomg.imagebff.user.domain.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class QueueService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val imageRepository: ImageRepository,
) {

    fun enqueueTask(userId: UUID, taskId: UUID) {
        val task = taskRepository.findById(taskId) ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        task.enqueue()
        taskRepository.save(task)
    }

    fun startTask(userId: UUID, taskId: UUID) {
        val task = taskRepository.findById(taskId) ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        task.start()
        taskRepository.save(task)
    }

    fun completeTask(taskId: UUID) {
        val task = taskRepository.findById(taskId) ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        task.complete()
        taskRepository.save(task)
    }

    fun failTask(taskId: UUID) {
        val task = taskRepository.findById(taskId) ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        task.fail()
        taskRepository.save(task)
    }

    fun retryTask(userId: UUID, taskId: UUID) {
        val task = taskRepository.findById(taskId) ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        task.retry()
        taskRepository.save(task)
    }
}