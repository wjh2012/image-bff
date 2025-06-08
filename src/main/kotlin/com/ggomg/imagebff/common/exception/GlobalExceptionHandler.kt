package com.ggomg.imagebff.common.exception

import BusinessException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(exception: BusinessException): ResponseEntity<ErrorResponse> {
        logger.error(exception) { exception.errorCode.description }
        val body = ErrorResponse(exception.errorCode)
        return ResponseEntity(body, exception.errorCode.httpStatus)
    }
}