package com.ggomg.imagebff.user.exception

import com.ggomg.imagebff.common.exception.ErrorCode
import org.springframework.http.HttpStatus

enum class UserErrorCode(
    override val code: String,
    override val description: String,
    override val httpStatus: HttpStatus
) : ErrorCode {
    NOT_EXISTS_USER("2000", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_USER("2001", "중복된 사용자입니다.", HttpStatus.CONFLICT),
    DUPLICATED_PASSWORD("2002", "기존 비밀번호와 변경하려는 비밀번호가 동일합니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("2003", "기존 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_NICKNAME("2004", "중복된 닉네임입니다.", HttpStatus.CONFLICT),
    DUPLICATE_EMAIL("2005", "중복된 이메일입니다.", HttpStatus.CONFLICT),
}
