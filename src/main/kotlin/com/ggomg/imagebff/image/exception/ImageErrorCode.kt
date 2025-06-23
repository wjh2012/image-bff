package com.ggomg.imagebff.image.exception

import com.ggomg.imagebff.common.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class ImageErrorCode(
    override val code: String,
    override val description: String,
    override val httpStatus: HttpStatus
) : ErrorCode {
    IMAGE_NOT_FOUND("2001", "해당 ID의 이미지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
}