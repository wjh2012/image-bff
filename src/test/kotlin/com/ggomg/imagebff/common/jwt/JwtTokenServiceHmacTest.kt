package com.ggomg.imagebff.common.jwt

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest(
    properties = [
        "jwt.mode=hmac",
        "jwt.hmac.secret=j0NrY1v5S5XuilYuEUFsLfprgRqnIO1l5/fijXSNqzQ=",
        "jwt.expiration=3600"
    ]
)
class JwtTokenServiceHmacTest(
    @Autowired
    val jwtTokenService: JwtTokenService
) {

    @Test
    fun `JWT 토큰 생성 성공`() {
        val email = "test@example.com"
        val token = jwtTokenService.generateToken(email)
        println("발급된 토큰: $token")
        assertNotNull(token)
        assertTrue(token.isNotBlank())
    }

    @Test
    fun `토큰 이메일 파싱 성공`() {
        val email = "test@example.com"
        val token = jwtTokenService.generateToken(email)
        val result = jwtTokenService.getEmailFromToken(token)
        assertEquals(email, result)
    }

    @Test
    fun `정상 토큰 검증 성공`() {
        val email = "test@example.com"
        val token = jwtTokenService.generateToken(email)
        val result = jwtTokenService.validateToken(token)
        assertTrue(result)
    }

    @Test
    fun `비정상 토큰 검증 실패`() {
        val invalidToken = "this.is.not.a.valid.jwt"
        val result = jwtTokenService.validateToken(invalidToken)
        assertFalse(result)
    }
}