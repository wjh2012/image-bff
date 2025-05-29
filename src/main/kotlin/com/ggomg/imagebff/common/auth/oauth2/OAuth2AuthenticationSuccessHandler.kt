package com.ggomg.imagebff.common.auth.oauth2

import com.ggomg.imagebff.common.auth.jwt.JwtTokenService
import com.ggomg.imagebff.user.infrastructure.repository.UserJpaRepository
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component


@Component
class OAuth2AuthenticationSuccessHandler(
    private val userJpaRepository: UserJpaRepository,
    private val jwtTokenService: JwtTokenService
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as? OAuth2User
        val email = oAuth2User?.getAttribute<String>("email")

        if (email == null) {
            response.sendRedirect("http://localhost:3000/login?error=missing_email")
            return
        }

        val user = userJpaRepository.findByEmail(email)

        if (user == null) {
            val cookie = Cookie("pending_email", email)
            cookie.path = "/"
            cookie.isHttpOnly = true
            cookie.maxAge = 60 * 5 // 5분 유효
            response.addCookie(cookie)

            response.sendRedirect("http://localhost:3000/oauth2/redirect")
            return
        }

        // 기존 회원 → JWT 생성 후 쿠키에 저장
        val token = jwtTokenService.generateToken(email)

        val cookie = Cookie("access_token", token)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.maxAge = 60 * 60 // 1시간

        response.addCookie(cookie)
        response.sendRedirect("http://localhost:3000")
    }
}