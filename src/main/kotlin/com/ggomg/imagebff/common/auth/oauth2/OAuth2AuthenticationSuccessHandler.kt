package com.ggomg.imagebff.common.auth.oauth2

import com.ggomg.imagebff.common.auth.jwt.JwtTokenService
import com.ggomg.imagebff.user.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2AuthenticationSuccessHandler(
    private val userRepository: UserRepository,
    private val jwtTokenService: JwtTokenService
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as? OAuth2User
        if (oAuth2User == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "OAuth2 user not found.")
            return
        }

        val email = oAuth2User.getAttribute<String>("email")
        if (email == null) {
            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Email not found in OAuth2 user attributes."
            )
            return
        }

        val user = userRepository.findByEmail(email)
        if (user == null) {
            response.contentType = "application/json"
            response.writer.write("""{"registration_required": true, "email": "$email"}""")
            return
        }

        val token = jwtTokenService.generateToken(email)
        response.contentType = "application/json"
        response.writer.write("""{"token": "$token"}""")
    }

}