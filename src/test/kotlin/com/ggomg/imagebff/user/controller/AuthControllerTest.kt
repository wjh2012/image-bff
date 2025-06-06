package com.ggomg.imagebff.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ggomg.imagebff.user.application.JwtAuthService
import com.ggomg.imagebff.user.model.jwt.login.JwtLoginRequest
import com.ggomg.imagebff.user.model.jwt.login.JwtLoginResponse
import com.ggomg.imagebff.user.model.jwt.register.JwtRegisterRequest
import com.ggomg.imagebff.user.model.jwt.register.JwtRegisterResponse
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
    private lateinit var jwtAuthService: JwtAuthService
    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        jwtAuthService = mockk()
        val jwtAuthController = JwtAuthController(jwtAuthService)
        mockMvc = MockMvcBuilders.standaloneSetup(jwtAuthController).build()
    }

    @Test
    fun `회원가입 API - 성공`() {
        // given
        val request = JwtRegisterRequest("홍길동", "test@example.com", "password123")
        val response = JwtRegisterResponse(token = "register-token")

        every { jwtAuthService.signUp(request) } returns response

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
        val request = JwtLoginRequest("test@example.com", "password123")
        val response = JwtLoginResponse(token = "login-token")

        every { jwtAuthService.login(request) } returns response

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