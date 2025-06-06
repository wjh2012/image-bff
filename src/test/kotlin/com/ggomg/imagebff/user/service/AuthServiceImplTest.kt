package com.ggomg.imagebff.user.service

import BusinessException
import com.ggomg.imagebff.common.auth.jwt.JwtTokenService
import com.ggomg.imagebff.auth.application.JwtAuthService
import com.ggomg.imagebff.user.domain.AuthType
import com.ggomg.imagebff.user.domain.User
import com.ggomg.imagebff.user.domain.UserRepository
import com.ggomg.imagebff.user.domain.UserRole
import com.ggomg.imagebff.user.exception.UserErrorCode
import com.ggomg.imagebff.user.model.jwt.login.JwtLoginRequest
import com.ggomg.imagebff.user.model.jwt.register.JwtRegisterRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.Test

class AuthServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var jwtTokenService: JwtTokenService
    private lateinit var jwtAuthService: JwtAuthService

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        passwordEncoder = mockk()
        jwtTokenService = mockk()
        jwtAuthService = JwtAuthService(userRepository, passwordEncoder, jwtTokenService)
    }

    @Test
    fun `register - 회원가입 성공`() {
        // given
        val request = JwtRegisterRequest("tester", "test@email.com", "plainPassword")
        val encodedPassword = "encodedPassword"
        val savedUser = User(
            name = request.name,
            email = request.email,
            password = encodedPassword,
            authType = AuthType.NORMAL,
            userRole = UserRole.ROLE_USER
        )

        every { passwordEncoder.encode(request.password) } returns encodedPassword
        every { userRepository.save(any()) } returns savedUser
        every { userRepository.findByEmail(request.email) } returns null
        every { jwtTokenService.generateToken(savedUser.email) } returns "mockToken"
        // when
        val response = jwtAuthService.signUp(request)

        // then
        assertEquals("mockToken", response.token)
        verify { userRepository.save(any()) }
    }

    @Test
    fun `login - 로그인 성공`() {
        // given
        val request = JwtLoginRequest("user@email.com", "password123")
        val user = User(
            name = "user",
            email = request.email,
            password = "encodedPassword",
            authType = AuthType.NORMAL,
            userRole = UserRole.ROLE_USER
        )

        every { userRepository.findByEmail(request.email) } returns user
        every { passwordEncoder.matches(request.password, user.password) } returns true
        every { jwtTokenService.generateToken(user.email) } returns "loginToken"

        // when
        val response = jwtAuthService.login(request)

        // then
        assertEquals("loginToken", response.token)
    }

    @Test
    fun `login - 존재하지 않는 사용자 예외`() {
        // given
        val request = JwtLoginRequest("noone@email.com", "password")
        every { userRepository.findByEmail(request.email) } returns null

        // then
        val exception = assertThrows<BusinessException> {
            jwtAuthService.login(request)
        }
        assertEquals(UserErrorCode.NOT_EXISTS_USER, exception.errorCode)
    }

    @Test
    fun `login - 비밀번호 불일치 예외`() {
        // given
        val request = JwtLoginRequest("user@email.com", "wrongPassword")
        val user = User(
            name = "user",
            email = request.email,
            password = "encodedPassword",
            authType = AuthType.NORMAL,
            userRole = UserRole.ROLE_USER
        )

        every { userRepository.findByEmail(request.email) } returns user
        every { passwordEncoder.matches(request.password, user.password) } returns false

        // then
        val exception = assertThrows<BusinessException> {
            jwtAuthService.login(request)
        }
        assertEquals(UserErrorCode.INVALID_PASSWORD, exception.errorCode)
    }
}
