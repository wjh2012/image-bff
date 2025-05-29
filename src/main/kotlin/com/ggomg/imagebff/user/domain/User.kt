package com.ggomg.imagebff.user.domain

class User(
    val id: Long? = null,
    val name: String,
    val email: String,
    val password: String,
    val authType: AuthType,
    val userRole: UserRole
) {
}