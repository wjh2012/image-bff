package com.ggomg.imagebff.task.domain

import java.time.LocalDateTime
import java.util.UUID

class Task(
    val id: UUID,
    val userId: UUID,
    private var name: String,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime
) {
    fun changeName(newName: String) {
        require(newName.isNotBlank()) { "이름은 비어 있을 수 없습니다." }
        this.name = newName
        this.updatedAt = LocalDateTime.now()
    }
}