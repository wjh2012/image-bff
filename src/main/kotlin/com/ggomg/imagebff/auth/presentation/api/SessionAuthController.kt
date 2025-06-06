package com.ggomg.imagebff.auth.presentation.api

import com.ggomg.imagebff.auth.application.SessionAuthService
import com.ggomg.imagebff.auth.model.login.LoginRequest
import com.ggomg.imagebff.auth.model.register.RegisterRequest
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class SessionAuthController(
    private val authService: SessionAuthService,
    private val authenticationManager: AuthenticationManager,
) {

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "사용자 회원가입을 진행한다.")
    fun register(
        @RequestBody registerRequest: RegisterRequest
    ): ResponseEntity<Map<String, String>> {
        authService.signUp(registerRequest)
        return ResponseEntity.ok(mapOf("message" to "회원가입 성공"))
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인을 진행한다.")
    fun login(
        @RequestBody loginRequest: LoginRequest,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, String>> {

        val authToken = UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        val auth = authenticationManager.authenticate(authToken)
        SecurityContextHolder.getContext().authentication = auth

        val session = request.getSession(true)  // 없으면 새로 생성

        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext()
        )

        return ResponseEntity.ok(mapOf("message" to "로그인 성공"))
    }
}