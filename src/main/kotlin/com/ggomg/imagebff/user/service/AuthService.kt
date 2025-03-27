package com.ggomg.imagebff.user.service

import com.ggomg.imagebff.user.model.login.LoginRequest
import com.ggomg.imagebff.user.model.login.LoginResponse
import com.ggomg.imagebff.user.model.register.RegisterRequest
import com.ggomg.imagebff.user.model.register.RegisterResponse

interface AuthService {

    fun register(registerRequest: RegisterRequest): RegisterResponse

    fun login(loginRequest: LoginRequest): LoginResponse

}