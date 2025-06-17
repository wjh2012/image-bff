package com.ggomg.imagebff.taskQueue.infrastructure

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class TaskProcessor {
    fun process(task: Task) {
        logger.info { "Processing task ${task.userId}:${task.jobId}" }
        Thread.sleep(300) // mock 처리 시간
    }
}