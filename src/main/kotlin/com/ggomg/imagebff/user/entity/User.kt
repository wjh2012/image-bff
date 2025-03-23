package com.ggomg.imagebff.user.entity

import com.ggomg.imagebff.common.base.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name="email", nullable = false, unique = true)
    val email: String,
    
    @Column(name="password", nullable = false)
    val password: String,
    
) : BaseEntity()
