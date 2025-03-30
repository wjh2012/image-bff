package com.ggomg.imagebff.common.jwt.jwtHeaderStrategy

import org.springframework.security.oauth2.jwt.JwsHeader

interface JwtHeaderStrategy {
    fun getJwsHeader(): JwsHeader
}