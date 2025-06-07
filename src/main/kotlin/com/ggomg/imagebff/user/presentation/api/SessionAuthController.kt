package com.ggomg.imagebff.user.presentation.api

import com.ggomg.imagebff.auth.model.register.RegisterRequest
import com.ggomg.imagebff.user.application.RegisterService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class SessionAuthController(
    private val authService: RegisterService,
) {

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "사용자 회원가입을 진행한다.")
    fun register(
        @RequestBody registerRequest: RegisterRequest
    ): ResponseEntity<Map<String, String>> {
        authService.register(registerRequest)
        return ResponseEntity.ok(mapOf("message" to "회원가입 성공"))
    }

}