package com.ggomg.imagebff.task.exception

import com.ggomg.imagebff.common.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class TaskErrorCode(
    override val code: String,
    override val description: String,
    override val httpStatus: HttpStatus
) : ErrorCode {
    TASK_NOT_FOUND("3001", "해당 ID의 작업을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_TASK_STATE("3002", "현재 상태에서 해당 상태로 전이할 수 없습니다.", HttpStatus.CONFLICT),
    EMPTY_TASK_NAME("3003", "작업 이름은 비어 있을 수 없습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_IN_TARGET_STATE("3004", "이미 해당 상태로 전이되어 있습니다.", HttpStatus.BAD_REQUEST);
}