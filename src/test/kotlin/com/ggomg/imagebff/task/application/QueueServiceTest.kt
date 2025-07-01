package com.ggomg.imagebff.task.application

import com.ggomg.imagebff.task.infrastructure.message.TaskImageEvent
import com.ggomg.imagebff.task.infrastructure.message.TaskRabbitMQPublisher
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
class QueueServiceUnitTest {

    @Autowired
    lateinit var taskRabbitMQPublisher: TaskRabbitMQPublisher


    @Test
    fun `OCR 이벤트 publish 통합 테스트`() {
        // given
        val event = TaskImageEvent(
            taskId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
        )

        // when
        taskRabbitMQPublisher.publishOcr(event)
        taskRabbitMQPublisher.publishValidation(event)
        taskRabbitMQPublisher.publishThumbnail(event)

        // then
        println("테스트 실행 완료 — 실제 로그 확인 필요")
    }
}