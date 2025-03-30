package com.ggomg.imagebff.user.service

import BusinessException
import com.ggomg.imagebff.common.jwt.JwtService
import com.ggomg.imagebff.user.entity.AuthType
import com.ggomg.imagebff.user.entity.User
import com.ggomg.imagebff.user.exception.UserErrorCode
import com.ggomg.imagebff.user.model.login.LoginRequest
import com.ggomg.imagebff.user.model.login.LoginResponse
import com.ggomg.imagebff.user.model.register.RegisterRequest
import com.ggomg.imagebff.user.model.register.RegisterResponse
import com.ggomg.imagebff.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
) : AuthService {

    override fun register(registerRequest: RegisterRequest): RegisterResponse {
        val encodedPassword = passwordEncoder.encode(registerRequest.password)
        val user = User(
            name = registerRequest.name,
            email = registerRequest.email,
            password = encodedPassword,
            authType = AuthType.NORMAL
        );
        userRepository.save(user)

        return RegisterResponse(success = true)
    }

    override fun login(loginRequest: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw BusinessException(
                UserErrorCode.NOT_EXISTS_USER,
                "user not found. userEmail: ${loginRequest.email}"
            )

        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            throw BusinessException(UserErrorCode.INVALID_PASSWORD, "invalid password")
        }

        return LoginResponse(success = true)
    }

}