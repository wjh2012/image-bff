package com.ggomg.imagebff.user.service

import com.ggomg.imagebff.user.entity.AuthType
import com.ggomg.imagebff.user.entity.User
import com.ggomg.imagebff.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository
) : AuthService {
    
    override fun registerNormalUser(name: String, email: String, password: String) {
        val user = User(name = name, email = email, password = password, authType = AuthType.NORMAL);
        userRepository.save(user)
    }
    
}