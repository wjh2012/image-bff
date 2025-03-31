package com.ggomg.imagebff.common.jwt


interface JwtTokenService {

    fun generateToken(): String

    fun validateToken(token: String): Boolean

}