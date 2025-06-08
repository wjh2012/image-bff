package com.ggomg.imagebff.auth.application

import com.ggomg.imagebff.auth.model.CustomUserDetails
import com.ggomg.imagebff.user.domain.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class CustomUserDetailService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        logger.info { "사용자 조회: ${email}" }

        val user =
            userRepository.findByEmail(email) ?: throw UsernameNotFoundException("User not found")
        val authorities = listOf(SimpleGrantedAuthority(user.userRole.name))
        return CustomUserDetails(user.id, user.email, user.password, authorities)
    }

}