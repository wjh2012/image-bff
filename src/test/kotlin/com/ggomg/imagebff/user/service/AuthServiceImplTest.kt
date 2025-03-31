package com.ggomg.imagebff.user.service

import BusinessException
import com.ggomg.imagebff.common.jwt.JwtTokenService
import com.ggomg.imagebff.user.entity.AuthType
import com.ggomg.imagebff.user.entity.User
import com.ggomg.imagebff.user.exception.UserErrorCode
import com.ggomg.imagebff.user.model.login.LoginRequest
import com.ggomg.imagebff.user.repository.UserRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class AuthServiceImplTest {

    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var passwordEncoder: PasswordEncoder

    @MockK
    lateinit var jwtTokenService: JwtTokenService

    lateinit var authService: AuthServiceImpl

    @BeforeEach
    fun setUp() {
        authService = AuthServiceImpl(userRepository, passwordEncoder, jwtTokenService)
    }

    @Test
    fun `정상 로그인`() {
        // given
        val request = LoginRequest(email = "test@example.com", password = "1234")
        val user = User(
            id = 1L,
            name = "홍길동",
            email = "test@example.com",
            password = "encodedPwd",
            authType = AuthType.NORMAL
        )

        every { userRepository.findByEmail(request.email) } returns user
        every { passwordEncoder.matches(request.password, user.password) } returns true

        // when
        val response = authService.login(request)

        // then
        assertTrue(response.success)
    }

    @Test
    fun `없는 유저일 경우 예외 발생`() {
        // given
        val request = LoginRequest(email = "nouser@example.com", password = "1234")
        every { userRepository.findByEmail(request.email) } returns null

        // then
        val exception = assertThrows<BusinessException> {
            authService.login(request)
        }
        assertEquals(UserErrorCode.NOT_EXISTS_USER, exception.errorCode)
    }

    @Test
    fun `비밀번호 틀릴 경우 예외 발생`() {
        // given
        val request = LoginRequest(email = "test@example.com", password = "wrongpass")
        val user = User(1L, "홍길동", "test@example.com", "encodedPwd", AuthType.NORMAL)

        every { userRepository.findByEmail(request.email) } returns user
        every { passwordEncoder.matches(request.password, user.password) } returns false

        // then
        val exception = assertThrows<BusinessException> {
            authService.login(request)
        }
        assertEquals(UserErrorCode.INVALID_PASSWORD, exception.errorCode)
    }
}
