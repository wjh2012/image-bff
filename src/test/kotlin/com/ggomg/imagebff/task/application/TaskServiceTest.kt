package com.ggomg.imagebff.task.application

import com.ggomg.imagebff.image.domain.Image
import com.ggomg.imagebff.image.domain.ImageRepository
import com.ggomg.imagebff.task.domain.Task
import com.ggomg.imagebff.task.domain.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class TaskServiceTest {

    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private val imageRepository: ImageRepository = mockk(relaxed = true)

    private lateinit var taskService: TaskService

    private val userId = UUID.randomUUID()
    private val taskId = UUID.randomUUID()
    private val imageId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        taskService = TaskService(taskRepository, imageRepository)
    }

    @Test
    fun `save는 task를 저장한다`() {
        taskService.save(userId, "새 작업")

        verify { taskRepository.save(any()) }
    }

    @Test
    fun `addImageToTask는 정상적으로 동작하면 task 저장 호출`() {
        val imageId = UUID.randomUUID()

        val task = mockk<Task>(relaxed = true)
        val image = mockk<Image>()

        every { image.id } returns imageId

        every { task.getUserId() } returns userId
        every { taskRepository.findByUserIdAndId(userId, taskId) } returns task
        every { imageRepository.findByUserIdAndId(userId, imageId) } returns image

        taskService.addImageToTask(userId, taskId, imageId)

        verify { taskRepository.save(task) }
    }

    @Test
    fun `addImagesToTask는 모든 이미지가 유효하고 권한이 맞으면 저장 호출`() {
        val task = mockk<Task>(relaxed = true)

        val imageId1 = UUID.randomUUID()
        val imageId2 = UUID.randomUUID()

        val image1 = mockk<Image>()
        val image2 = mockk<Image>()

        every { image1.id } returns imageId1
        every { image1.userId } returns userId

        every { image2.id } returns imageId2
        every { image2.userId } returns userId

        every { task.getUserId() } returns userId
        every { taskRepository.findByUserIdAndId(userId, taskId) } returns task
        every {
            imageRepository.findAllByUserIdAndIdIn(
                userId,
                listOf(imageId1, imageId2)
            )
        } returns listOf(image1, image2)

        taskService.addImagesToTask(userId, taskId, listOf(imageId1, imageId2))

        verify(exactly = 2) { task.addImage(any()) }
        verify { taskRepository.save(task) }
    }

    @Test
    fun `changeTaskName은 이름 변경 후 저장을 수행한다`() {
        val task = mockk<Task>(relaxed = true)

        every { taskRepository.findByUserIdAndId(userId, taskId) } returns task

        taskService.changeTaskName(userId, taskId, "새 이름")

        verify { task.changeName("새 이름") }
        verify { taskRepository.save(task) }
    }

    @Test
    fun `delete는 task가 존재하면 삭제 수행`() {
        val task = mockk<Task>()

        every { taskRepository.findByUserIdAndId(userId, taskId) } returns task

        taskService.delete(userId, taskId)

        verify { taskRepository.delete(task) }
    }

}