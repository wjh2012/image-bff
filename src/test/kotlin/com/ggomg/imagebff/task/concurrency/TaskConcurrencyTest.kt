package com.ggomg.imagebff.task.concurrency

import BusinessException
import com.ggomg.imagebff.task.application.QueueService
import com.ggomg.imagebff.task.domain.InProgressState
import com.ggomg.imagebff.task.domain.QueuedState
import com.ggomg.imagebff.task.domain.Task
import com.ggomg.imagebff.task.infrastructure.TaskRepositoryImpl
import com.ggomg.imagebff.task.exception.TaskErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.Collections
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class TaskConcurrencyTest @Autowired constructor(
    private val queueService: QueueService,
    private val taskRepository: TaskRepositoryImpl,
) {
    private lateinit var taskId: UUID
    private lateinit var userId: UUID

    @BeforeEach
    fun setUp() {
        taskId = UUID.randomUUID()
        userId = UUID.randomUUID()
        val task = Task(
            id = taskId,
            userId = userId,
            name = "taskName",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        taskRepository.save(task)
    }

    @Test
    fun `락 없으면 enqueue와 start 동시에 실행시 start만 실패하고 QueuedState 여야 한다`() {
        val executor = Executors.newFixedThreadPool(2)
        val ready = CountDownLatch(2)
        val go = CountDownLatch(1)
        val done = CountDownLatch(2)
        val exceptions = Collections.synchronizedList(mutableListOf<String>())

        executor.submit {
            ready.countDown()
            go.await()
            try {
                val t = taskRepository.findByUserIdAndId(userId, taskId)!!
                t.enqueue()
                taskRepository.save(t)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                done.countDown()
            }
        }

        executor.submit {
            ready.countDown()
            go.await()
            try {
                val t = taskRepository.findByUserIdAndId(userId, taskId)!!
                t.start() // NewState 상태이므로 실패
                taskRepository.save(t)
            } catch (e: BusinessException) {
                if (e.errorCode == TaskErrorCode.INVALID_TASK_STATE) {
                    exceptions.add("start")
                } else {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                done.countDown()
            }
        }

        ready.await()
        go.countDown()
        done.await()

        val final = taskRepository.findByUserIdAndId(userId, taskId)!!
        assertThat(exceptions).containsExactly("start")
        assertThat(final.getStatus()::class).isEqualTo(QueuedState::class)
    }

    @Test
    fun `비관적 락 있으면 enqueue와 start 동시에 실행시 최종 InProgressState 여야 한다`() {
        val executor = Executors.newFixedThreadPool(2)
        val ready = CountDownLatch(2)
        val go = CountDownLatch(1)
        val done = CountDownLatch(2)

        executor.submit {
            ready.countDown()
            go.await()
            try {
                queueService.enqueueTask(userId, taskId)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                done.countDown()
            }
        }

        executor.submit {
            ready.countDown()
            go.await()
            try {
                queueService.startTask(userId, taskId)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                done.countDown()
            }
        }

        ready.await()
        go.countDown()
        done.await()

        val final = taskRepository.findByUserIdAndId(userId, taskId)!!
        assertThat(final.getStatus()::class).isEqualTo(InProgressState::class)
    }
}
