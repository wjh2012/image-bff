package com.ggomg.imagebff.task.application

import BusinessException
import com.fasterxml.uuid.Generators
import com.ggomg.imagebff.image.domain.ImageRepository
import com.ggomg.imagebff.image.exception.ImageErrorCode
import com.ggomg.imagebff.task.domain.Task
import com.ggomg.imagebff.task.domain.TaskRepository
import com.ggomg.imagebff.task.exception.TaskErrorCode
import com.ggomg.imagebff.user.domain.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val imageRepository: ImageRepository,
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

    fun addImageToTask(userId: UUID, taskId: UUID, imageId: UUID) {
        val task = taskRepository.findById(taskId) ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)
        val image = imageRepository.findById(imageId) ?: throw BusinessException(ImageErrorCode.IMAGE_NOT_FOUND)

        // 사용자 권한 검사
        if (task.getUserId() != userId || image.userId != userId) {
            throw IllegalAccessException("다른 사용자의 리소스에 접근할 수 없습니다.")
        }

        taskRepository.save(task)
    }

    fun addImagesToTask(userId: UUID, taskId: UUID, imageIds: List<UUID>) {
        val task = taskRepository.findById(taskId)
            ?: throw BusinessException(TaskErrorCode.TASK_NOT_FOUND)

        val images = imageRepository.findAllByIds(imageIds)
            .associateBy { it.id } // Map<UUID, Image>

        // 1. 존재하지 않는 이미지 ID 사전 필터링
        val notFoundIds = imageIds.filterNot { images.containsKey(it) }
        if (notFoundIds.isNotEmpty()) {
            throw BusinessException(ImageErrorCode.IMAGE_NOT_FOUND)
        }

        // 2. 권한 체크 한 번에
        if (task.getUserId() != userId || images.values.any { it.userId != userId }) {
            throw IllegalAccessException("다른 사용자의 리소스에 접근할 수 없습니다.")
        }

        // 3. 중복 추가 방지
        val existingImageIds = task.getTaskImages().map { it.imageId }.toSet()
        val newImages = images.values.filterNot { existingImageIds.contains(it.id) }

        // 4. Task에 이미지 추가
        newImages.forEach { task.addImage(it) }

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