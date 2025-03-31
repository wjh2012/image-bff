package com.ggomg.imagebff.common.jwt

import com.ggomg.imagebff.common.jwt.model.CustomUserDetails
import com.ggomg.imagebff.user.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email) ?: throw UsernameNotFoundException("User not found")
        val authorities = listOf(SimpleGrantedAuthority(user.userRole.name))
        return CustomUserDetails(user.email, user.password, authorities)
    }

}