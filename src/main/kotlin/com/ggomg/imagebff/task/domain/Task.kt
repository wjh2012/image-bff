package com.ggomg.imagebff.task.domain

import java.time.LocalDateTime
import java.util.UUID

class Task(
    val id: UUID,
    val userId: UUID,
    var name: String,
    var status: TaskState = RegisteredState,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime
) {
    fun changeName(newName: String) {
        require(newName.isNotBlank()) { "이름은 비어 있을 수 없습니다." }
        this.name = newName
        this.updatedAt = LocalDateTime.now()
    }

    /** 큐에 등록(RegisteredState → QueuedState) */
    fun enqueue() = status.enqueue(this)

    /** 실행 시작(QueuedState → InProgressState) */
    fun start() = status.start(this)

    /** 실행 완료(InProgressState → CompletedState) */
    fun complete() = status.complete(this)

    /** 실행 실패(InProgressState → FailedState) */
    fun fail() = status.fail(this)

    /** 실패 후 재시도(FailedState → RegisteredState) */
    fun retry() = status.retry(this)
}