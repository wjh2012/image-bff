package com.ggomg.imagebff.common.jwt


interface JwtService {

    fun generateToken(): String

    fun validateToken(token: String): Boolean

}