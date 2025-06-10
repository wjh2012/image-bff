package com.ggomg.imagebff.taskQueue.domain

import java.util.UUID

interface TaskQueueRepository {

    fun loadByUserIdOrderByPositionAsc(userId: UUID): TaskQueue?

    fun load(userId: UUID): TaskQueue?

    fun save(taskQueue: TaskQueue)

    fun delete(taskQueue: TaskQueue)
}