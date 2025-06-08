package com.ggomg.imagebff.task.domain

import BusinessException
import com.ggomg.imagebff.task.exception.TaskErrorCode
import java.time.LocalDateTime


/**
 * Task 상태를 큐 밖(Registered), 큐 안(Queued), 진행 중(InProgress), 실패(Failed), 완료(Completed)로 분리
 */
sealed class TaskState {
    abstract fun enqueue(task: Task)         // 큐로 진입
    abstract fun start(task: Task)           // 실행 시작
    abstract fun complete(task: Task)        // 실행 완료
    abstract fun fail(task: Task)            // 실행 실패
    abstract fun retry(task: Task)           // 실패 후 재시도
}

/** 큐에 등록만 된 상태(아직 큐에 진입 전) */
object RegisteredState : TaskState() {
    override fun enqueue(task: Task) {
        task.status = QueuedState
        task.updatedAt = LocalDateTime.now()
    }

    override fun start(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "큐 밖에 있는 작업은 바로 실행할 수 없습니다."
        )
    }

    override fun complete(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "아직 실행되지 않은 작업은 완료할 수 없습니다."
        )
    }

    override fun fail(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "아직 실행되지 않은 작업은 실패 상태가 될 수 없습니다."
        )
    }

    override fun retry(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "아직 실패되지 않은 작업은 재시도할 수 없습니다."
        )
    }
}

/** 큐에 들어가 실행 대기 중인 상태 */
object QueuedState : TaskState() {
    override fun enqueue(task: Task) {
        throw BusinessException(
            TaskErrorCode.ALREADY_IN_TARGET_STATE,
            "이미 큐에 진입된 작업입니다."
        )
    }

    override fun start(task: Task) {
        task.status = InProgressState
        task.updatedAt = LocalDateTime.now()
    }

    override fun complete(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "아직 실행되지 않은 작업은 완료할 수 없습니다."
        )
    }

    override fun fail(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "아직 실행되지 않은 작업은 실패 상태가 될 수 없습니다."
        )
    }

    override fun retry(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "실패되지 않은 작업은 재시도할 수 없습니다."
        )
    }
}

/** 실제 실행 중인 상태 */
object InProgressState : TaskState() {
    override fun enqueue(task: Task) {
        throw BusinessException(
            TaskErrorCode.ALREADY_IN_TARGET_STATE,
            "실행 중인 작업은 큐에 다시 진입할 수 없습니다."
        )
    }

    override fun start(task: Task) {
        throw BusinessException(
            TaskErrorCode.ALREADY_IN_TARGET_STATE,
            "이미 실행 중인 작업입니다."
        )
    }

    override fun complete(task: Task) {
        task.status = CompletedState
        task.updatedAt = LocalDateTime.now()
    }

    override fun fail(task: Task) {
        task.status = FailedState
        task.updatedAt = LocalDateTime.now()
    }

    override fun retry(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "실행 중인 작업은 완료 또는 실패 처리해야 합니다."
        )
    }
}

/** 실행 실패된 상태 */
object FailedState : TaskState() {
    override fun enqueue(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "실패된 작업은 재시도 이후에만 큐에 진입시킬 수 있습니다."
        )
    }

    override fun start(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "실패된 작업은 재시도로만 실행 가능합니다."
        )
    }

    override fun complete(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "실패된 작업은 바로 완료할 수 없습니다."
        )
    }

    override fun fail(task: Task) {
        throw BusinessException(
            TaskErrorCode.ALREADY_IN_TARGET_STATE,
            "이미 실패된 작업입니다."
        )
    }

    override fun retry(task: Task) {
        task.status = RegisteredState
        task.updatedAt = LocalDateTime.now()
    }
}

/** 실행 완료된 상태 */
object CompletedState : TaskState() {
    override fun enqueue(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "완료된 작업은 큐에 진입할 수 없습니다."
        )
    }

    override fun start(task: Task) {
        throw BusinessException(
            TaskErrorCode.ALREADY_IN_TARGET_STATE,
            "이미 완료된 작업입니다."
        )
    }

    override fun complete(task: Task) {
        throw BusinessException(
            TaskErrorCode.ALREADY_IN_TARGET_STATE,
            "이미 완료된 작업입니다."
        )
    }

    override fun fail(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "완료된 작업은 실패 상태로 전이할 수 없습니다."
        )
    }

    override fun retry(task: Task) {
        throw BusinessException(
            TaskErrorCode.INVALID_TASK_STATE,
            "완료된 작업은 재시도할 수 없습니다."
        )
    }
}
