package com.ggomg.imagebff.common.exception

import java.time.LocalDateTime

data class ErrorResponse(
    val code: String,
    val message: String,
    val timeStamp: LocalDateTime
) {
    constructor(errorCode: ErrorCode) : this(
        code = errorCode.code,
        message = errorCode.description,
        timeStamp = LocalDateTime.now()
    )
}
