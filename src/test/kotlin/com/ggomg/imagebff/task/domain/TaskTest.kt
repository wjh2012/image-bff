package com.ggomg.imagebff.task.domain

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.junit5.JUnit5Asserter.fail

class TaskTest {
    private lateinit var task: Task
    private val userId = UUID.randomUUID()
    private val now = LocalDateTime.now()

    @BeforeEach
    fun setUp() {
        task = Task(
            id = UUID.randomUUID(),
            userId = userId,
            name = "초기 작업",
            createdAt = now,
            updatedAt = now
        )
    }

    @Test
    fun `changeName은 정상적으로 이름을 변경한다`() {
        task.changeName("새 이름")
        assertDoesNotThrow { task.changeName("또 다른 이름") }
    }

    @Test
    fun `changeName은 공백일 경우 예외를 발생시킨다`() {
        assertThrows<IllegalArgumentException> {
            task.changeName("  ")
        }
    }

    @Test
    fun `addImage는 이미지가 중복되지 않을 때 추가된다`() {
        val imageId = UUID.randomUUID()
        task.addImage(imageId)

        val taskImages = task.getTaskImages()
        assertEquals(1, taskImages.size)
        assertEquals(imageId, taskImages[0].imageId)
    }

    @Test
    fun `addImage는 중복된 이미지 ID가 있을 경우 예외 발생`() {
        val imageId = UUID.randomUUID()
        task.addImage(imageId)

        val exception = assertThrows<IllegalArgumentException> {
            task.addImage(imageId)
        }

        assertEquals("이미 추가된 이미지입니다.", exception.message)
    }

    @Test
    fun `removeImage는 존재하는 이미지 ID일 경우 성공적으로 제거된다`() {
        val imageId = UUID.randomUUID()
        task.addImage(imageId)
        val taskImageId = task.getTaskImages().first().id

        task.removeImage(taskImageId)

        assertTrue(task.getTaskImages().isEmpty())
    }

    @Test
    fun `removeImage는 존재하지 않는 이미지 ID일 경우 예외 발생`() {
        assertThrows<IllegalArgumentException> {
            task.removeImage(UUID.randomUUID())
        }
    }

    @Test
    fun `상태 전이는 정상적으로 동작해야 한다`() {
        val initialStatus = task.getStatus()
        task.enqueue()
        val enqueuedStatus = task.getStatus()
        if (initialStatus == enqueuedStatus) {
            fail("상태 전이가 일어나지 않았습니다.")
        }
    }
}