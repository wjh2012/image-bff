package com.ggomg.imagebff.task.domain

import java.util.UUID

interface TaskRepository {

    fun findById(id: UUID): Task?

    fun findAllByUserId(userId: UUID): List<Task>

    fun findByUserIdAndId(userId: UUID, id: UUID): Task?

    fun save(task: Task): Task

    fun delete(task: Task)
}