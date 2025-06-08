package com.ggomg.imagebff.user.infrastructure.repository

import com.ggomg.imagebff.user.infrastructure.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserJpaRepository : JpaRepository<UserEntity, UUID>, UserJpaRepositoryCustom {

    fun findByEmail(email: String): UserEntity?

    fun existsByEmail(email: String): Boolean

}