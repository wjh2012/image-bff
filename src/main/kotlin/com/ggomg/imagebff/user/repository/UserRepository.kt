package com.ggomg.imagebff.user.repository

import com.ggomg.imagebff.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>, UserRepositoryCustom {
    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}