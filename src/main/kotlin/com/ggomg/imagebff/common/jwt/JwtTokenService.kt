package com.ggomg.imagebff.common.jwt


interface JwtTokenService {

    fun generateToken(email: String): String

    fun validateToken(token: String): Boolean

    fun getEmailFromToken(token: String): String

}