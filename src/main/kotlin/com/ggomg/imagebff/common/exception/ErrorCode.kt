package com.ggomg.imagebff.common.exception

import org.springframework.http.HttpStatus

interface ErrorCode {
    val code: String
    val description: String
    val httpStatus: HttpStatus
}