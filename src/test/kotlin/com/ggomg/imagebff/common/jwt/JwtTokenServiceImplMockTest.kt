package com.ggomg.imagebff.common.jwt

import com.ggomg.imagebff.common.auth.jwt.JwtTokenTokenServiceImpl
import com.ggomg.imagebff.common.auth.jwt.jwtHeaderStrategy.JwtHeaderStrategy
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.security.oauth2.jwt.*

class JwtTokenServiceImplMockTest {

    private lateinit var jwtEncoder: JwtEncoder
    private lateinit var jwtDecoder: JwtDecoder
    private lateinit var jwtHeaderStrategy: JwtHeaderStrategy
    private lateinit var jwtService: JwtTokenTokenServiceImpl

    @BeforeEach
    fun setUp() {
        jwtEncoder = mockk()
        jwtDecoder = mockk()
        jwtHeaderStrategy = mockk()
        jwtService = JwtTokenTokenServiceImpl(jwtEncoder, jwtDecoder, jwtHeaderStrategy)
    }

    @Test
    fun `JWT 토큰 검증 성공 - token_type 이 LOGIN 인 경우`() {
        // given
        val claims = mapOf("token_type" to "LOGIN")
        val mockJwt = mockk<Jwt>()
        every { mockJwt.claims } returns claims
        every { jwtDecoder.decode("valid-token") } returns mockJwt

        // when
        val result = jwtService.validateToken("valid-token")

        // then
        assertTrue(result)
    }

    @Test
    fun `JWT 토큰 검증 실패 - token_type 불일치 (REGISTER)`() {
        // given
        val claims = mapOf("token_type" to "REGISTER")
        val mockJwt = mockk<Jwt>()
        every { mockJwt.claims } returns claims
        every { jwtDecoder.decode("wrong-type-token") } returns mockJwt

        // when
        val result = jwtService.validateToken("wrong-type-token")

        // then
        assertFalse(result)
    }

    @Test
    fun `JWT 토큰 검증 실패 - 디코딩 예외 발생`() {
        // given
        every { jwtDecoder.decode("invalid-token") } throws JwtException("Invalid")

        // when
        val result = jwtService.validateToken("invalid-token")

        // then
        assertFalse(result)
    }

}
