package com.ggomg.imagebff.user.infrastructure.entity

import com.ggomg.imagebff.common.base.BaseEntity
import com.ggomg.imagebff.user.domain.AuthType
import com.ggomg.imagebff.user.domain.UserRole
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    var id: UUID,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", nullable = false)
    val authType: AuthType,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val userRole: UserRole,

    ) : BaseEntity()
