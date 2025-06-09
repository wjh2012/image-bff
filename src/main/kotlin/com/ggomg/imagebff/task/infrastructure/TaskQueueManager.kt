package com.ggomg.imagebff.task.infrastructure

import com.ggomg.imagebff.task.domain.TaskRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TaskQueueManager(
    private val repo: TaskRepository,
    private val rabbit: RabbitTemplate,
    private val txManager: org.springframework.transaction.PlatformTransactionManager
) {

    @Value("\${app.queue.batch-size:10}")
    private var batchSize: Int = 10

    private val log = LoggerFactory.getLogger(QueueManager::class.java)

    @Scheduled(fixedDelayString = "\${app.queue.poll-interval-ms:1000}")
    fun pollAndPublish() {
        val txTemplate = TransactionTemplate(txManager)
        txTemplate.execute {
            // 1) ENQUEUED 상태의 태스크 잠금 조회
            val tasks = repo.fetchEnqueuedForUpdate(batchSize)
            if (tasks.isEmpty()) return@execute null

            // 2) 상태 전이 → PROCESSING
            val now = LocalDateTime.now()
            tasks.forEach { task ->
                task.status = Task.Status.PROCESSING
                task.updatedAt = now
                repo.save(task)
            }

            // 3) RabbitMQ 발행
            tasks.forEach { task ->
                val msg: Message = MessageBuilder
                    .withBody(task.payload.toByteArray(StandardCharsets.UTF_8))
                    .setHeader("task_id", task.id)
                    .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                    .build()
                rabbit.send(RabbitConfig.TASK_QUEUE, msg)
                log.info("Published task id={}", task.id)
            }

            null
        }
    }
}