package com.ggomg.imagebff.taskQueue.domain

import java.time.LocalDateTime
import java.util.UUID

data class QueueEntry(
    val taskId: UUID,
    val userId: UUID,
    var position: Int,
    val enqueuedAt: LocalDateTime,
) {
    fun moveTo(newPosition: Int) {
        require(newPosition >= 0) { "새 위치(newPosition)는 0 이상이어야 합니다." }
        position = newPosition
    }
}
