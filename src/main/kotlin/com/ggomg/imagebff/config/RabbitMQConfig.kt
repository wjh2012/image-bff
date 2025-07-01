package com.ggomg.imagebff.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig(
    private val rabbitMQProperties: RabbitMQProperties
) {

    @Bean
    fun messageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin {
        return RabbitAdmin(connectionFactory)
    }

    // OCR
    @Bean
    fun ocrQueue(): Queue {
        return Queue("ocr.queue", true)  // durable = true
    }

    @Bean
    fun ocrExchange(): DirectExchange {
        return DirectExchange(rabbitMQProperties.ocr.exchange)
    }

    @Bean
    fun ocrBinding(): Binding {
        return BindingBuilder.bind(ocrQueue())
            .to(ocrExchange())
            .with(rabbitMQProperties.ocr.routingKey)
    }

    // Validation
    @Bean
    fun validationQueue(): Queue {
        return Queue("validation.queue", true)
    }

    @Bean
    fun validationExchange(): DirectExchange {
        return DirectExchange(rabbitMQProperties.validation.exchange)
    }

    @Bean
    fun validationBinding(): Binding {
        return BindingBuilder.bind(validationQueue())
            .to(validationExchange())
            .with(rabbitMQProperties.validation.routingKey)
    }

    // Thumbnail
    @Bean
    fun thumbnailQueue(): Queue {
        return Queue("thumbnail.queue", true)
    }

    @Bean
    fun thumbnailExchange(): DirectExchange {
        return DirectExchange(rabbitMQProperties.thumbnail.exchange)
    }

    @Bean
    fun thumbnailBinding(): Binding {
        return BindingBuilder.bind(thumbnailQueue())
            .to(thumbnailExchange())
            .with(rabbitMQProperties.thumbnail.routingKey)
    }

}