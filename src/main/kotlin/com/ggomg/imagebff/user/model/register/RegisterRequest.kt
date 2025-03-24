package com.ggomg.imagebff.user.model.register

data class RegisterRequest (
    val name: String,
    val email: String,
    val password: String,
)