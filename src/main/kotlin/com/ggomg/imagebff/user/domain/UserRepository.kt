package com.ggomg.imagebff.user.domain


interface UserRepository {
    fun findByEmail(email: String): User?

    fun findById(id: Long): User?

    fun save(user: User): User

    fun delete(id: Long)
}