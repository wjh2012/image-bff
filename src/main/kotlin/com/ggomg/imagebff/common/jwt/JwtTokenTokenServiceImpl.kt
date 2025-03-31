package com.ggomg.imagebff.common.jwt

import com.ggomg.imagebff.common.jwt.jwtHeaderStrategy.JwtHeaderStrategy
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class JwtTokenTokenServiceImpl(
    val jwtEncoder: JwtEncoder,
    val jwtDecoder: JwtDecoder,
    val jwtHeaderStrategy: JwtHeaderStrategy
) :
    JwtTokenService {

    override fun generateToken(email: String): String {
        val now = Instant.now()
        val claims = JwtClaimsSet.builder()
            .issuer("ggomg")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .subject(email)
            .build()

        val jwsHeader = jwtHeaderStrategy.getJwsHeader()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    override fun validateToken(token: String): Boolean {
        return try {
            jwtDecoder.decode(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getEmailFromToken(token: String): String {
        return try {
            val decodedJwt = jwtDecoder.decode(token)
            decodedJwt.claims["sub"]?.toString() ?: throw IllegalArgumentException("Email claim not found in token.")
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid token.")
        }
    }
}