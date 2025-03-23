package com.ggomg.imagebff.user.service

import com.ggomg.imagebff.user.entity.User
import com.ggomg.imagebff.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository
) : AuthService {


    override fun registerUser(email: String, password: String) {
        val user = User(email = email, password = password);
        userRepository.save(user)
    }
}