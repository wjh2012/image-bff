package com.ggomg.imagebff.taskQueue.application

import com.ggomg.imagebff.taskQueue.domain.TaskQueueRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class TaskQueueService(
    private val taskQueueRepository: TaskQueueRepository,
) {
    @Transactional
    fun reorderTasks(userId: UUID, taskId: UUID, toIndex: Int) {
        val taskQueue = taskQueueRepository.loadByUserIdOrderByPositionAsc(userId)
            ?: throw IllegalArgumentException("task not found")

        taskQueue.reorder(taskId, toIndex)
        taskQueueRepository.save(taskQueue)
    }

    @Transactional
    fun pollTask(userId: UUID): UUID {
        val taskQueue = taskQueueRepository.load(userId)
            ?: throw IllegalArgumentException("task not found")

        val polledTask = taskQueue.pollNext() ?: throw IllegalArgumentException("task not found")
        taskQueueRepository.save(taskQueue)

        return polledTask.taskId
    }
}