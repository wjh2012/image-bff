package com.ggomg.imagebff.user.service

interface AuthService {

    fun registerUser(email: String, password: String)
}