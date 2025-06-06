package com.ggomg.imagebff.user.domain

import java.util.UUID

class User(
    val id: UUID,
    val name: String,
    val email: String,
    val password: String,
    val authType: AuthType,
    val userRole: UserRole
) {
}