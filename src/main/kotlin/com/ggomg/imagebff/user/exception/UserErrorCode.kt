package com.ggomg.imagebff.user.exception

import com.ggomg.imagebff.common.exception.ErrorCode

enum class UserErrorCode(
    override val code: String,
    override val description: String
) : ErrorCode {
    NOT_EXISTS_USER("2000", "존재하지 않는 사용자입니다."),
    DUPLICATE_USER("2001", "중복된 사용자입니다."),
    DUPLICATED_PASSWORD("2002", "기존 비밀번호와 변경하려는 비밀번호가 동일합니다."),
    INVALID_PASSWORD("2003", "기존 비밀번호가 일치하지 않습니다."),
    DUPLICATE_NICKNAME("2004", "중복된 닉네임입니다."),
    DUPLICATE_EMAIL("2005", "중복된 이메일입니다."),
    NOT_MATCH_AUTH_TYPE("2006", "올바른 경로로 로그인 해주시길 바랍니다."),
    NOT_EXISTS_UNIVERSITY("2007", "존재하지 않는 대학교입니다.");
}
