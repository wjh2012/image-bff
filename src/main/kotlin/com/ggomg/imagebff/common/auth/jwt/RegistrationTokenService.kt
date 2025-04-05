package com.ggomg.imagebff.common.auth.jwt

interface RegistrationTokenService {

    fun generateToken(email: String): String

    fun validateToken(token: String): Boolean

    fun getEmailFromToken(token: String): String


}