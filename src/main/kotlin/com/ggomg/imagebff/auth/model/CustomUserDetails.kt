package com.ggomg.imagebff.auth.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class CustomUserDetails(
    private val id: UUID,
    private val username: String,
    private val password: String,
    private val authorities: List<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): List<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    fun getId(): UUID {
        return id;
    }

}