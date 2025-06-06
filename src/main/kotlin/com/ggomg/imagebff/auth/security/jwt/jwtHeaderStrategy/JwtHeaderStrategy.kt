package com.ggomg.imagebff.auth.security.jwt.jwtHeaderStrategy

import org.springframework.security.oauth2.jwt.JwsHeader

interface JwtHeaderStrategy {
    fun getJwsHeader(): JwsHeader
}