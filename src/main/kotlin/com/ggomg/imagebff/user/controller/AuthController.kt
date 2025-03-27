package com.ggomg.imagebff.user.controller

import com.ggomg.imagebff.user.model.login.LoginRequest
import com.ggomg.imagebff.user.model.login.LoginResponse
import com.ggomg.imagebff.user.model.register.RegisterRequest
import com.ggomg.imagebff.user.model.register.RegisterResponse
import com.ggomg.imagebff.user.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "사용자 회원가입을 진행한다.")
    fun register(registerRequest: RegisterRequest): ResponseEntity<RegisterResponse> {
        authService.register(
            registerRequest
        )
        return ResponseEntity.ok(RegisterResponse(true))
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인을 진행한다.")
    fun login(loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        authService.login(loginRequest)
        return ResponseEntity.ok(LoginResponse(true))
    }

}