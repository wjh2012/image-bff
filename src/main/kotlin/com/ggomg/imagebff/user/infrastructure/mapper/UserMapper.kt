package com.ggomg.imagebff.user.infrastructure.mapper

import com.ggomg.imagebff.user.domain.User
import com.ggomg.imagebff.user.infrastructure.entity.UserEntity
import org.springframework.stereotype.Component

@Component
object UserMapper {
    fun toDomain(entity: UserEntity): User = User(
        id = entity.id,
        name = entity.name,
        email = entity.email,
        password = entity.password,
        authType = entity.authType,
        userRole = entity.userRole
    )

    fun toEntity(domain: User): UserEntity = UserEntity(
        id = domain.id,
        name = domain.name,
        email = domain.email,
        password = domain.password,
        authType = domain.authType,
        userRole = domain.userRole
    )
}