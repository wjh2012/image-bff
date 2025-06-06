package com.ggomg.imagebff.auth.model.register

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
)