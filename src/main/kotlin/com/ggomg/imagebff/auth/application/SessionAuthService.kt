package com.ggomg.imagebff.auth.application

import BusinessException
import com.ggomg.imagebff.user.domain.AuthType
import com.ggomg.imagebff.user.domain.User
import com.ggomg.imagebff.user.domain.UserRepository
import com.ggomg.imagebff.user.domain.UserRole
import com.ggomg.imagebff.user.exception.UserErrorCode
import com.ggomg.imagebff.auth.model.login.LoginRequest
import com.ggomg.imagebff.auth.model.register.RegisterRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SessionAuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun signUp(request: RegisterRequest) {
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
        userRepository.save(user)
    }

    fun login(request: LoginRequest): String {
        val user = userRepository.findByEmail(request.email)
            ?: throw BusinessException(
                UserErrorCode.NOT_EXISTS_USER,
                "user not found. userEmail: ${request.email}"
            )

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BusinessException(UserErrorCode.INVALID_PASSWORD, "invalid password")
        }

        return user.email
    }
}