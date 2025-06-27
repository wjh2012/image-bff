package com.ggomg.imagebff.task.application

import BusinessException
import com.ggomg.imagebff.task.domain.TaskRepository
import com.ggomg.imagebff.task.exception.TaskErrorCode
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class QueueService(
    private val taskLockingRepository: TaskLockingRepository,
    private val taskRepository: TaskRepository,
) {

    fun enqueueTask(userId: UUID, taskId: UUID) {
        val task = taskLockingRepository.findByUserIdAndIdForUpdate(userId, taskId)
            ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        task.enqueue()
        taskRepository.save(task)
    }

    fun startTask(userId: UUID, taskId: UUID) {
        val task = taskLockingRepository.findByUserIdAndIdForUpdate(userId, taskId)
            ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        task.start()
        taskRepository.save(task)
    }

    fun completeTask(userId: UUID, taskId: UUID) {
        val task = taskLockingRepository.findByUserIdAndIdForUpdate(userId, taskId)
            ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        task.complete()
        taskRepository.save(task)
    }

    fun failTask(userId: UUID, taskId: UUID) {
        val task = taskLockingRepository.findByUserIdAndIdForUpdate(userId, taskId)
            ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        task.fail()
        taskRepository.save(task)
    }

    fun retryTask(userId: UUID, taskId: UUID) {
        val task = taskLockingRepository.findByUserIdAndIdForUpdate(userId, taskId)
            ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        task.retry()
        taskRepository.save(task)
    }
}