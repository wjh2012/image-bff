package com.ggomg.imagebff.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.ggomg.imagebff.auth.model.login.LoginRequest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.authentication.AuthenticationConverter

class JsonUsernamePasswordAuthenticationConverter(
    private val objectMapper: ObjectMapper
) : AuthenticationConverter {

    override fun convert(request: jakarta.servlet.http.HttpServletRequest): org.springframework.security.core.Authentication? {
        if (request.method != "POST" || request.contentType?.startsWith(MediaType.APPLICATION_JSON_VALUE) != true) {
            return null
        }

        return try {
            val loginRequest = objectMapper.readValue(request.inputStream, LoginRequest::class.java)
            UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        } catch (ex: Exception) {
            null
        }
    }
}
