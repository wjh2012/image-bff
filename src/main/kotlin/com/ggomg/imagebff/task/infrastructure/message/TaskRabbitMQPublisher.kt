package com.ggomg.imagebff.task.infrastructure.message

import com.ggomg.imagebff.config.RabbitMQProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class TaskRabbitMQPublisher(
    private val rabbitMqTemplate: RabbitTemplate,
    private val rabbitMQProperties: RabbitMQProperties
) {
    fun publishOcr(event: TaskImageEvent) {
        val exchange = rabbitMQProperties.ocr.exchange
        val routingKey = rabbitMQProperties.ocr.routingKey
        rabbitMqTemplate.convertAndSend(exchange, routingKey, event)
    }

    fun publishValidation(event: TaskImageEvent) {
        val exchange = rabbitMQProperties.validation.exchange
        val routingKey = rabbitMQProperties.validation.routingKey
        rabbitMqTemplate.convertAndSend(exchange, routingKey, event)
    }

    fun publishThumbnail(event: TaskImageEvent) {
        val exchange = rabbitMQProperties.thumbnail.exchange
        val routingKey = rabbitMQProperties.thumbnail.routingKey
        rabbitMqTemplate.convertAndSend(exchange, routingKey, event)
    }
}