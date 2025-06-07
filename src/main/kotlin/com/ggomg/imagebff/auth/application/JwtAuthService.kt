package com.ggomg.imagebff.auth.application

import BusinessException
import com.ggomg.imagebff.auth.security.jwt.JwtTokenService
import com.ggomg.imagebff.user.domain.UserRepository
import com.ggomg.imagebff.user.exception.UserErrorCode
import com.ggomg.imagebff.auth.model.login.LoginRequest
import com.ggomg.imagebff.auth.model.login.LoginResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class JwtAuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenService: JwtTokenService,
) {

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw BusinessException(
                UserErrorCode.NOT_EXISTS_USER,
                "user not found. userEmail: ${request.email}"
            )

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BusinessException(UserErrorCode.INVALID_PASSWORD, "invalid password")
        }

        val token = jwtTokenService.generateToken(email = user.email)

        return LoginResponse(token = token)
    }
}