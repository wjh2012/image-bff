package com.ggomg.imagebff.user.domain

import java.util.UUID


interface UserRepository {
    fun findById(id: UUID): User?

    fun findByEmail(email: String): User?

    fun save(user: User): User

    fun delete(id: UUID)
}