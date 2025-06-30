package com.ggomg.imagebff.task.infrastructure

import com.ggomg.imagebff.task.domain.Task
import com.ggomg.imagebff.task.domain.TaskEventPublisher
import com.ggomg.imagebff.task.infrastructure.message.TaskImageEvent
import com.ggomg.imagebff.task.infrastructure.message.TaskRabbitMQPublisher
import org.springframework.stereotype.Component

@Component
class TaskPublisherImpl(
    private val taskRabbitMQPublisher: TaskRabbitMQPublisher
) : TaskEventPublisher {
    override fun publishEnqueuedEvents(task: Task) {
        val taskEvent = TaskImageEvent(taskId = task.getId(), userId = task.getUserId())
        taskRabbitMQPublisher.publishThumbnail(taskEvent)
        taskRabbitMQPublisher.publishValidation(taskEvent)
        taskRabbitMQPublisher.publishOcr(taskEvent)
    }

}