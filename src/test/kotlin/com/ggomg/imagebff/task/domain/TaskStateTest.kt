package com.ggomg.imagebff.task.domain

import BusinessException
import com.ggomg.imagebff.task.exception.TaskErrorCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class TaskStateTest {

    private lateinit var task: Task

    @BeforeEach
    fun setup() {
        task = Task(
            id = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            name = "상태 테스트",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    // -------------------
    // Registered 상태
    // -------------------
    @Test
    fun `Registered 상태는 enqueue 시 QueuedState로 전이된다`() {
        task.enqueue()
        assertEquals(QueuedState, task.getStatus())
    }

    @Test
    fun `Registered 상태에서 start 시 예외 발생`() {
        val ex = assertThrows<BusinessException> { task.start() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    @Test
    fun `Registered 상태에서 complete 시 예외 발생`() {
        val ex = assertThrows<BusinessException> { task.complete() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    @Test
    fun `Registered 상태에서 fail 시 예외 발생`() {
        val ex = assertThrows<BusinessException> { task.fail() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    @Test
    fun `Registered 상태에서 retry 시 예외 발생`() {
        val ex = assertThrows<BusinessException> { task.retry() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    // -------------------
    // Queued 상태
    // -------------------
    @Test
    fun `Queued 상태는 start 시 InProgressState로 전이된다`() {
        task.enqueue()
        task.start()
        assertEquals(InProgressState, task.getStatus())
    }

    @Test
    fun `Queued 상태에서 enqueue 시 예외 발생`() {
        task.enqueue()
        val ex = assertThrows<BusinessException> { task.enqueue() }
        assertEquals(TaskErrorCode.ALREADY_IN_TARGET_STATE, ex.errorCode)
    }

    @Test
    fun `Queued 상태에서 complete 시 예외 발생`() {
        task.enqueue()
        val ex = assertThrows<BusinessException> { task.complete() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    @Test
    fun `Queued 상태에서 fail 시 예외 발생`() {
        task.enqueue()
        val ex = assertThrows<BusinessException> { task.fail() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    @Test
    fun `Queued 상태에서 retry 시 예외 발생`() {
        task.enqueue()
        val ex = assertThrows<BusinessException> { task.retry() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    // -------------------
    // InProgress 상태
    // -------------------
    @Test
    fun `InProgress 상태에서 complete 시 CompletedState로 전이된다`() {
        task.enqueue()
        task.start()
        task.complete()
        assertEquals(CompletedState, task.getStatus())
    }

    @Test
    fun `InProgress 상태에서 fail 시 FailedState로 전이된다`() {
        task.enqueue()
        task.start()
        task.fail()
        assertEquals(FailedState, task.getStatus())
    }

    @Test
    fun `InProgress 상태에서 enqueue 시 예외 발생`() {
        task.enqueue()
        task.start()
        val ex = assertThrows<BusinessException> { task.enqueue() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    @Test
    fun `InProgress 상태에서 start 시 예외 발생`() {
        task.enqueue()
        task.start()
        val ex = assertThrows<BusinessException> { task.start() }
        assertEquals(TaskErrorCode.ALREADY_IN_TARGET_STATE, ex.errorCode)
    }

    @Test
    fun `InProgress 상태에서 retry 시 예외 발생`() {
        task.enqueue()
        task.start()
        val ex = assertThrows<BusinessException> { task.retry() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    // -------------------
    // Failed 상태
    // -------------------
    @Test
    fun `Failed 상태에서 retry 시 RegisteredState로 전이된다`() {
        task.enqueue()
        task.start()
        task.fail()
        task.retry()
        assertEquals(RegisteredState, task.getStatus())
    }


    @Test
    fun `Failed 상태에서 enqueue 시 예외 발생`() {
        task.enqueue()
        task.start()
        task.fail()
        val ex = assertThrows<BusinessException> { task.enqueue() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    @Test
    fun `Failed 상태에서 start 시 예외 발생`() {
        task.enqueue()
        task.start()
        task.fail()
        val ex = assertThrows<BusinessException> { task.start() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    @Test
    fun `Failed 상태에서 complete 시 예외 발생`() {
        task.enqueue()
        task.start()
        task.fail()
        val ex = assertThrows<BusinessException> { task.complete() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    @Test
    fun `Failed 상태에서 fail 시 예외 발생`() {
        task.enqueue()
        task.start()
        task.fail()
        val ex = assertThrows<BusinessException> { task.fail() }
        assertEquals(TaskErrorCode.ALREADY_IN_TARGET_STATE, ex.errorCode)
    }

    // -------------------
    // Completed 상태
    // -------------------
    @Test
    fun `Completed 상태에서 enqueue 시 예외 발생`() {
        task.enqueue()
        task.start()
        task.complete()
        val ex = assertThrows<BusinessException> { task.enqueue() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    @Test
    fun `Completed 상태에서 start 시 예외 발생`() {
        task.enqueue()
        task.start()
        task.complete()
        val ex = assertThrows<BusinessException> { task.start() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    @Test
    fun `Completed 상태에서 complete 시 예외 발생`() {
        task.enqueue()
        task.start()
        task.complete()
        val ex = assertThrows<BusinessException> { task.complete() }
        assertEquals(TaskErrorCode.ALREADY_IN_TARGET_STATE, ex.errorCode)
    }

    @Test
    fun `Completed 상태에서 retry 시 예외 발생`() {
        task.enqueue()
        task.start()
        task.complete()
        val ex = assertThrows<BusinessException> { task.retry() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }

    @Test
    fun `Completed 상태에서 fail 시 예외 발생`() {
        task.enqueue()
        task.start()
        task.complete()
        val ex = assertThrows<BusinessException> { task.fail() }
        assertEquals(TaskErrorCode.INVALID_TASK_STATE, ex.errorCode)
    }
}
