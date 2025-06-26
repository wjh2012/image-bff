package com.ggomg.imagebff.task.domain

import BusinessException
import com.ggomg.imagebff.task.exception.TaskErrorCode

sealed class TaskState {
    abstract fun enqueue(task: Task)
    abstract fun start(task: Task)
    abstract fun complete(task: Task)
    abstract fun fail(task: Task)
    abstract fun retry(task: Task)
}

// 1. Registered
object RegisteredState : TaskState() {
    override fun enqueue(task: Task) {
        task.transitionTo(QueuedState)
    }

    override fun start(task: Task) = invalid("큐 밖에 있는 작업은 바로 실행할 수 없습니다.")
    override fun complete(task: Task) = invalid("아직 실행되지 않은 작업은 완료할 수 없습니다.")
    override fun fail(task: Task) = invalid("아직 실행되지 않은 작업은 실패 상태가 될 수 없습니다.")
    override fun retry(task: Task) = invalid("아직 실패되지 않은 작업은 재시도할 수 없습니다.")
}

// 2. Queued
object QueuedState : TaskState() {
    override fun enqueue(task: Task) = already()
    override fun start(task: Task) {
        task.transitionTo(InProgressState)
    }

    override fun complete(task: Task) = invalid("아직 실행되지 않은 작업은 완료할 수 없습니다.")
    override fun fail(task: Task) = invalid("아직 실행되지 않은 작업은 실패 상태가 될 수 없습니다.")
    override fun retry(task: Task) = invalid("실패되지 않은 작업은 재시도할 수 없습니다.")
}

// 3. InProgress
object InProgressState : TaskState() {
    override fun enqueue(task: Task) = invalid("이미 시작한 작업은 되돌릴 수 없습니다.")
    override fun start(task: Task) = already()
    override fun complete(task: Task) {
        task.transitionTo(CompletedState)
    }

    override fun fail(task: Task) {
        task.transitionTo(FailedState)
    }

    override fun retry(task: Task) = invalid("실행 중인 작업은 완료 또는 실패 처리해야 합니다.")
}

// 4. Failed
object FailedState : TaskState() {
    override fun enqueue(task: Task) = invalid("실패된 작업은 재시도 이후에만 큐에 진입시킬 수 있습니다.")
    override fun start(task: Task) = invalid("실패된 작업은 재시도로만 실행 가능합니다.")
    override fun complete(task: Task) = invalid("실패된 작업은 바로 완료할 수 없습니다.")
    override fun fail(task: Task) = already()
    override fun retry(task: Task) {
        task.transitionTo(RegisteredState)
    }
}

// 5. Completed
object CompletedState : TaskState() {
    override fun enqueue(task: Task) = invalid("완료된 작업은 큐에 진입할 수 없습니다.")
    override fun start(task: Task) = invalid("완료된 작업은 큐에 시작할 수 없습니다.")
    override fun complete(task: Task) = already()
    override fun fail(task: Task) = invalid("완료된 작업은 실패 상태로 전이할 수 없습니다.")
    override fun retry(task: Task) = invalid("완료된 작업은 재시도할 수 없습니다.")
}

// 공통 오류 처리
private fun invalid(message: String): Nothing {
    throw BusinessException(TaskErrorCode.INVALID_TASK_STATE, message)
}

private fun already(): Nothing {
    throw BusinessException(TaskErrorCode.ALREADY_IN_TARGET_STATE, "이미 해당 상태입니다.")
}
