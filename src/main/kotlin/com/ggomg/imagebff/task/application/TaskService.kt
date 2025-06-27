package com.ggomg.imagebff.task.application

import BusinessException
import com.fasterxml.uuid.Generators
import com.ggomg.imagebff.image.domain.ImageRepository
import com.ggomg.imagebff.image.exception.ImageErrorCode
import com.ggomg.imagebff.task.domain.Task
import com.ggomg.imagebff.task.domain.TaskRepository
import com.ggomg.imagebff.task.exception.TaskErrorCode
import com.ggomg.imagebff.task.model.TaskImageResponse
import com.ggomg.imagebff.task.model.TaskResponse
import com.ggomg.imagebff.task.model.TasksResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class TaskService(
    private val taskRepository: TaskRepository,
    private val imageRepository: ImageRepository,
) {

    fun createTask(userId: UUID, taskName: String) {
        val task = Task(
            id = Generators.timeBasedEpochGenerator().generate(),
            userId = userId,
            name = taskName,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
        taskRepository.save(task)
    }

    fun getAllTaskByUserId(userId: UUID): TasksResponse {
        val tasks = taskRepository.findAllByUserId(userId)

        val taskResponses = tasks.map { task ->
            TaskResponse(
                id = task.getId().toString(),
                name = task.getName(),
                status = task.getStatus().toString(),
                createdAt = task.getCreatedAt(),
                updatedAt = task.getUpdatedAt(),
                images = task.getTaskImages().map { taskImage ->
                    TaskImageResponse(
                        id = taskImage.id.toString(),
                        taskId = taskImage.taskId.toString(),
                        imageId = taskImage.imageId.toString()
                    )
                }
            )
        }

        return TasksResponse(taskResponses)
    }

    fun addImageToTask(userId: UUID, taskId: UUID, imageId: UUID) {
        val task = getTaskForUser(userId, taskId)
        val image = imageRepository.findByUserIdAndId(userId, imageId)
            ?: throw BusinessException(ImageErrorCode.IMAGE_NOT_FOUND)

        task.addImage(image.id)
        taskRepository.save(task)
    }

    fun addImagesToTask(userId: UUID, taskId: UUID, imageIds: List<UUID>) {
        val task = getTaskForUser(userId, taskId)
        val images = imageRepository.findAllByUserIdAndIdIn(userId, imageIds)
            .associateBy { it.id }

        val notFoundIds = imageIds.filterNot { images.containsKey(it) }
        if (notFoundIds.isNotEmpty()) {
            throw BusinessException(ImageErrorCode.IMAGE_NOT_FOUND)
        }

        imageIds.forEach { task.addImage(it) }
        taskRepository.save(task)
    }

    fun changeTaskName(userId: UUID, taskId: UUID, newName: String) {
        val task = getTaskForUser(userId, taskId)
        task.changeName(newName)
        taskRepository.save(task)
    }

    fun deleteTaskByUserIdAndTaskId(userId: UUID, taskId: UUID) {
        val task = getTaskForUser(userId, taskId)
        taskRepository.delete(task)
    }

    private fun getTaskForUser(userId: UUID, taskId: UUID): Task {
        return taskRepository.findByUserIdAndId(userId, taskId)
            ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)
    }
}
