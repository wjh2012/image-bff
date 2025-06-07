package com.ggomg.imagebff.user.application

import BusinessException
import com.fasterxml.uuid.Generators
import com.ggomg.imagebff.auth.model.register.RegisterRequest
import com.ggomg.imagebff.user.domain.AuthType
import com.ggomg.imagebff.user.domain.User
import com.ggomg.imagebff.user.domain.UserRepository
import com.ggomg.imagebff.user.domain.UserRole
import com.ggomg.imagebff.user.exception.UserErrorCode
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun register(request: RegisterRequest) {
        userRepository.findByEmail(request.email)?.let {
            throw BusinessException(
                UserErrorCode.DUPLICATE_USER,
                "이미 등록된 사용자입니다: ${request.email}"
            )
        }

        val encodedPassword = passwordEncoder.encode(request.password)

        val generatedUUID = Generators.timeBasedEpochGenerator().generate()

        val user = User(
            id = generatedUUID,
            name = request.name,
            email = request.email,
            password = encodedPassword,
            authType = AuthType.NORMAL,
            userRole = UserRole.ROLE_USER
        )
        userRepository.save(user)
    }
}