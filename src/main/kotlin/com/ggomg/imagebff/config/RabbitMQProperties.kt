package com.ggomg.imagebff.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "rabbitmq.image")
class RabbitMQProperties {

    val ocr = TaskQueue()
    val validation = TaskQueue()
    val thumbnail = TaskQueue()

    class TaskQueue {
        lateinit var exchange: String
        lateinit var routingKey: String
    }

}