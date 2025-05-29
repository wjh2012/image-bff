package com.ggomg.imagebff.common.auth.filter

import com.ggomg.imagebff.common.auth.jwt.RegistrationTokenService
import com.ggomg.imagebff.common.auth.model.CustomUserDetails
import com.ggomg.imagebff.user.domain.UserRole
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.FilterChain
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import kotlin.test.Test

class RegistrationTemporaryTokenFilterTest {

    private lateinit var registrationTokenService: RegistrationTokenService
    private lateinit var userDetailsService: UserDetailsService
    private lateinit var registrationTemporaryTokenFilter: RegistrationTemporaryTokenFilter
    private lateinit var filterChain: FilterChain

    @BeforeEach
    fun setUp() {
        registrationTokenService = mockk()
        userDetailsService = mockk()
        registrationTemporaryTokenFilter =
            RegistrationTemporaryTokenFilter(registrationTokenService, userDetailsService)
        filterChain = mockk(relaxed = true)
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `유효한 등록 토큰이 있는 경우 인증 객체가 설정된다`() {
        val token = "validRegToken"
        val email = "user@example.com"

        every { registrationTokenService.validateToken(token) } returns true
        every { registrationTokenService.getEmailFromToken(token) } returns email

        val authorities = listOf(SimpleGrantedAuthority(UserRole.ROLE_USER.name))
        val userDetails: UserDetails =
            CustomUserDetails(username = "username", password = "password", authorities = authorities)
        every { userDetailsService.loadUserByUsername(email) } returns userDetails

        val request = MockHttpServletRequest().apply {
            addHeader("Authorization", "Bearer $token")
        }
        val response = MockHttpServletResponse()

        registrationTemporaryTokenFilter.doFilter(request, response, filterChain)

        val authentication = SecurityContextHolder.getContext().authentication
        assertNotNull(authentication, "인증 객체가 생성되어야 합니다.")

        verify { filterChain.doFilter(request, response) }
    }
}