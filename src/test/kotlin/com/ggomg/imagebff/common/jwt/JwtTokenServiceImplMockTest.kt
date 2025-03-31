package com.ggomg.imagebff.common.jwt

import com.ggomg.imagebff.common.jwt.jwtHeaderStrategy.JwtHeaderStrategy
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
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
    fun `JWT 토큰 생성 성공`() {
        // given
        val email = "test@example.com"

        val mockJwt = mockk<Jwt>()
        every { mockJwt.tokenValue } returns "mocked-token"
        every { jwtEncoder.encode(any()) } returns mockJwt
        every { jwtHeaderStrategy.getJwsHeader() } returns
                JwsHeader.with(SignatureAlgorithm.RS256).build()

        // when
        val token = jwtService.generateToken(email)

        // then
        assertEquals("mocked-token", token)
        verify { jwtEncoder.encode(any()) }
    }

    @Test
    fun `JWT 토큰 검증 성공`() {
        every { jwtDecoder.decode("valid-token") } returns mockk()

        val result = jwtService.validateToken("valid-token")

        assertTrue(result)
    }

    @Test
    fun `JWT 토큰 검증 실패`() {
        every { jwtDecoder.decode("invalid-token") } throws JwtException("Invalid")

        val result = jwtService.validateToken("invalid-token")

        assertFalse(result)
    }

}
