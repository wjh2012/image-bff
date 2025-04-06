package com.ggomg.imagebff.common.auth.jwt

import com.ggomg.imagebff.common.auth.jwt.JwtTokenTokenServiceImpl
import com.ggomg.imagebff.common.auth.jwt.jwtHeaderStrategy.JwtHeaderStrategy
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.SecurityContext
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.*
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

class JwtTokenServiceImpTest {

    @Test
    fun `JWT 토큰 생성-검증 성공`() {
        val email = "test@example.com"

        // RSA 키쌍
        val keyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.genKeyPair()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey

        // Nimbus JWT Encoder
        val rsaKey = RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .build()
        val jwkSet = JWKSet(rsaKey)
        val jwkSource = ImmutableJWKSet<SecurityContext>(jwkSet)
        val encoder = NimbusJwtEncoder(jwkSource)

        // Nimbus JWT Decoder 
        val decoder = NimbusJwtDecoder.withPublicKey(publicKey).build()

        // 헤더 전략
        val headerStrategy = object : JwtHeaderStrategy {
            override fun getJwsHeader(): JwsHeader {
                return JwsHeader.with(SignatureAlgorithm.RS256).build()
            }
        }

        // 실제 서비스 인스턴스
        val service = JwtTokenTokenServiceImpl(encoder, decoder, headerStrategy)

        // 토큰 생성 및 검증
        val token = service.generateToken(email)
        assertTrue(service.validateToken(token))
    }
}
