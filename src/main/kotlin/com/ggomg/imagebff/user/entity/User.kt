package com.ggomg.imagebff.user.entity

import com.ggomg.imagebff.common.base.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

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
