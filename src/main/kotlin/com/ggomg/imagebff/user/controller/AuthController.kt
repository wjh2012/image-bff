package com.ggomg.imagebff.user.controller

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
        authService.registerUser(registerRequest.email, registerRequest.password)
        return ResponseEntity.ok(RegisterResponse(true))
    }

}