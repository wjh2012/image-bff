package com.ggomg.imagebff.common.auth.service

import com.ggomg.imagebff.common.auth.model.CustomUserDetails
import com.ggomg.imagebff.user.infrastructure.repository.UserJpaRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    private val userJpaRepository: UserJpaRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userJpaRepository.findByEmail(email) ?: throw UsernameNotFoundException("User not found")
        val authorities = listOf(SimpleGrantedAuthority(user.userRole.name))
        return CustomUserDetails(user.email, user.password, authorities)
    }

}