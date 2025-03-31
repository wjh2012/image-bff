package com.ggomg.imagebff.common.jwt

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test


@SpringBootTest(
    properties = [
        "jwt.mode=rsa",
        "jwt.rsa.public-key-path=jwt/public.pem",
        "jwt.rsa.private-key-path=jwt/private.pem",
        "jwt.expiration=3600"
    ],
)
class JwtTokenServiceRsaTest(
    @Autowired
    val jwtTokenService: JwtTokenService
) {

    @Test
    fun `JWT 토큰 생성 성공`() {
        val token = jwtTokenService.generateToken()
        println("발급된 토큰: $token")
        assertNotNull(token)
        assertTrue(token.isNotBlank())
    }

    @Test
    fun `정상 토큰 검증 성공`() {
        val token = jwtTokenService.generateToken()
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