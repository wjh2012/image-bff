package com.ggomg.imagebff.common.jwt

import com.ggomg.imagebff.common.jwt.model.CustomUserDetails
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import jakarta.servlet.DispatcherType
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import kotlin.test.Test

class JwtAuthenticationFilterTest {

    private val jwtTokenService = mockk<JwtTokenService>()
    private val userDetailsService = mockk<UserDetailsService>()
    private val filter = JwtAuthenticationFilter(jwtTokenService, userDetailsService)

    @AfterEach
    fun clearContext() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `유효한 토큰이 있을 때 SecurityContext에 인증 객체가 설정되어야 한다`() {
        // given
        val token = "valid.jwt.token"
        val email = "test@example.com"
        val userDetails = CustomUserDetails(email, "password", emptyList())

        val request = mockk<HttpServletRequest>()
        val response = mockk<HttpServletResponse>()
        val filterChain = mockk<FilterChain>(relaxed = true)

        every { request.getHeader("Authorization") } returns "Bearer $token"
        every { jwtTokenService.validateToken(token) } returns true
        every { jwtTokenService.getEmailFromToken(token) } returns email
        every { userDetailsService.loadUserByUsername(email) } returns userDetails

        // 필수 mock 설정
        every { request.getAttribute(any()) } returns null
        every { request.setAttribute(any(), any()) } just Runs
        every { request.removeAttribute(any()) } just Runs
        every { request.dispatcherType } returns DispatcherType.REQUEST

        // when
        filter.doFilter(request, response, filterChain)

        // then
        val authentication = SecurityContextHolder.getContext().authentication
        assertNotNull(authentication)
        assertEquals(email, authentication.name)
    }

    @Test
    fun `Authorization 헤더가 없으면 SecurityContext에 인증 객체가 설정되지 않아야 한다`() {
        // given
        val request = mockk<HttpServletRequest>()
        val response = mockk<HttpServletResponse>()
        val filterChain = mockk<FilterChain>(relaxed = true)

        every { request.getHeader("Authorization") } returns null
        every { request.getAttribute(any()) } returns null
        every { request.setAttribute(any(), any()) } just Runs
        every { request.removeAttribute(any()) } just Runs
        every { request.dispatcherType } returns DispatcherType.REQUEST

        // when
        filter.doFilter(request, response, filterChain)

        // then
        val authentication = SecurityContextHolder.getContext().authentication
        assertNull(authentication)
    }

    @Test
    fun `유효하지 않은 토큰이면 SecurityContext에 인증 객체가 설정되지 않아야 한다`() {
        // given
        val token = "invalid.jwt.token"

        val request = mockk<HttpServletRequest>()
        val response = mockk<HttpServletResponse>()
        val filterChain = mockk<FilterChain>(relaxed = true)

        every { request.getHeader("Authorization") } returns "Bearer $token"
        every { jwtTokenService.validateToken(token) } returns false
        every { request.getAttribute(any()) } returns null
        every { request.setAttribute(any(), any()) } just Runs
        every { request.removeAttribute(any()) } just Runs
        every { request.dispatcherType } returns DispatcherType.REQUEST

        // when
        filter.doFilter(request, response, filterChain)

        // then
        val authentication = SecurityContextHolder.getContext().authentication
        assertNull(authentication)
    }


    @Test
    fun `토큰이 유효하지만 이메일 추출 중 예외가 발생하면 인증 객체가 설정되지 않아야 한다`() {
        val token = "valid.jwt.token"

        val request = mockk<HttpServletRequest>()
        val response = mockk<HttpServletResponse>()
        val filterChain = mockk<FilterChain>(relaxed = true)

        every { request.getHeader("Authorization") } returns "Bearer $token"
        every { jwtTokenService.validateToken(token) } returns true
        every { jwtTokenService.getEmailFromToken(token) } throws IllegalArgumentException("이메일 없음")

        every { request.getAttribute(any()) } returns null
        every { request.setAttribute(any(), any()) } just Runs
        every { request.removeAttribute(any()) } just Runs
        every { request.dispatcherType } returns DispatcherType.REQUEST

        // when
        filter.doFilter(request, response, filterChain)

        // then
        val authentication = SecurityContextHolder.getContext().authentication
        assertNull(authentication)
    }


}