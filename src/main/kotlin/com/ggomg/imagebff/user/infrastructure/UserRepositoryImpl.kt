package com.ggomg.imagebff.user.infrastructure

import com.ggomg.imagebff.user.domain.User
import com.ggomg.imagebff.user.domain.UserRepository
import com.ggomg.imagebff.user.infrastructure.mapper.UserMapper
import com.ggomg.imagebff.user.infrastructure.repository.UserJpaRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

private val logger = KotlinLogging.logger {}

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
    private val userMapper: UserMapper
) : UserRepository {

    override fun findByEmail(email: String): User? {
        logger.info { "Checking for user: $email" }
        val entity = userJpaRepository.findByEmail(email)  // nullable 그대로 유지
        return entity?.let { userMapper.toDomain(it) }  // entity 없으면 null 리턴
    }

    override fun save(user: User): User {
        val userEntity = userMapper.toEntity(user)
        val savedUserEntity = userJpaRepository.save(userEntity)
        val userDomain = userMapper.toDomain(savedUserEntity)
        return userDomain
    }

    override fun delete(id: Long) {
        val userEntity = userJpaRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("User not found")
        userJpaRepository.delete(userEntity)
    }

}