package com.ggomg.imagebff.user.application

import BusinessException
import com.ggomg.imagebff.common.auth.jwt.JwtTokenService
import com.ggomg.imagebff.user.domain.AuthType
import com.ggomg.imagebff.user.domain.User
import com.ggomg.imagebff.user.domain.UserRepository
import com.ggomg.imagebff.user.domain.UserRole
import com.ggomg.imagebff.user.exception.UserErrorCode
import com.ggomg.imagebff.user.model.login.LoginRequest
import com.ggomg.imagebff.user.model.login.LoginResponse
import com.ggomg.imagebff.user.model.register.RegisterRequest
import com.ggomg.imagebff.user.model.register.RegisterResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenService: JwtTokenService,
) {
    @Transactional
    fun signUp(request: RegisterRequest): RegisterResponse {
        if (userRepository.findByEmail(request.email) != null) {
            throw BusinessException(
                UserErrorCode.DUPLICATE_USER,
                "이미 등록된 사용자입니다: ${request.email}"
            )
        }
        val encodedPassword = passwordEncoder.encode(request.password)
        val user = User(
            name = request.name,
            email = request.email,
            password = encodedPassword,
            authType = AuthType.NORMAL,
            userRole = UserRole.ROLE_USER
        )
        val save = userRepository.save(user)
        val token = jwtTokenService.generateToken(save.email)
        return RegisterResponse(token = token)
    }

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