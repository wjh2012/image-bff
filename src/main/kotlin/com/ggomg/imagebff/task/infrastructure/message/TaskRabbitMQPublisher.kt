package com.ggomg.imagebff.task.infrastructure.message

import com.ggomg.imagebff.config.RabbitMQProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class TaskRabbitMQPublisher(
    private val rabbitMqTemplate: RabbitTemplate,
    private val rabbitMQProperties: RabbitMQProperties
) {
    fun publishOcr(event: TaskImageEvent) {
        val exchange = rabbitMQProperties.ocr.exchange
        val routingKey = rabbitMQProperties.ocr.routingKey
        rabbitMqTemplate.convertAndSend(exchange, routingKey, event)
        logger.info { "Publishing OCR event: $event" }
    }

    fun publishValidation(event: TaskImageEvent) {
        val exchange = rabbitMQProperties.validation.exchange
        val routingKey = rabbitMQProperties.validation.routingKey
        rabbitMqTemplate.convertAndSend(exchange, routingKey, event)
        logger.info { "Publishing Validation event: $event" }
    }

    fun publishThumbnail(event: TaskImageEvent) {
        val exchange = rabbitMQProperties.thumbnail.exchange
        val routingKey = rabbitMQProperties.thumbnail.routingKey
        rabbitMqTemplate.convertAndSend(exchange, routingKey, event)
        logger.info { "Publishing Thumbnail event: $event" }
    }
}