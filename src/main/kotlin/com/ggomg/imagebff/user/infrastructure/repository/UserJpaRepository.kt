package com.ggomg.imagebff.user.infrastructure.repository

import com.ggomg.imagebff.user.infrastructure.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserEntity, Long>, UserJpaRepositoryCustom {

    fun findByEmail(email: String): UserEntity?

    fun existsByEmail(email: String): Boolean

}