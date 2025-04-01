package com.ggomg.imagebff.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ggomg.imagebff.user.model.login.LoginRequest
import com.ggomg.imagebff.user.model.login.LoginResponse
import com.ggomg.imagebff.user.model.register.RegisterRequest
import com.ggomg.imagebff.user.model.register.RegisterResponse
import com.ggomg.imagebff.user.service.AuthService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.test.Test

class AuthControllerTest {

    private lateinit var mockMvc: MockMvc
    private lateinit var authService: AuthService
    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        authService = mockk()
        val authController = AuthController(authService)
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build()
    }

    @Test
    fun `회원가입 API - 성공`() {
        // given
        val request = RegisterRequest("홍길동", "test@example.com", "password123")
        val response = RegisterResponse(token = "register-token")

        every { authService.register(request) } returns response

        // when & then
        mockMvc.post("/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.token") { value("register-token") }
        }
    }

    @Test
    fun `로그인 API - 성공`() {
        // given
        val request = LoginRequest("test@example.com", "password123")
        val response = LoginResponse(token = "login-token")

        every { authService.login(request) } returns response

        // when & then
        mockMvc.post("/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.token") { value("login-token") }
        }
    }
}