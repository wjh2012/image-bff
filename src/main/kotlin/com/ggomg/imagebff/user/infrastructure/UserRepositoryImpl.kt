package com.ggomg.imagebff.user.infrastructure

import com.ggomg.imagebff.user.domain.User
import com.ggomg.imagebff.user.domain.UserRepository
import com.ggomg.imagebff.user.infrastructure.repository.UserJpaRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {
    override fun findByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): User? {
        TODO("Not yet implemented")
    }

    override fun save(user: User): User {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}