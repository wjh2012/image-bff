package com.ggomg.imagebff.user.infrastructure

import com.ggomg.imagebff.user.domain.User
import com.ggomg.imagebff.user.domain.UserRepository
import com.ggomg.imagebff.user.infrastructure.mapper.UserMapper
import com.ggomg.imagebff.user.infrastructure.repository.UserJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
    private val userMapper: UserMapper
) : UserRepository {

    override fun findByEmail(email: String): User? {
        val entity = userJpaRepository.findByEmail(email)
        return entity?.let { userMapper.toDomain(it) }
    }

    override fun findById(id: Long): User? {
        val entity = userJpaRepository.findByIdOrNull(id)
        return entity?.let { userMapper.toDomain(it) }
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