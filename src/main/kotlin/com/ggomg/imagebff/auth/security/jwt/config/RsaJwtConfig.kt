package com.ggomg.imagebff.auth.security.jwt.config

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.security.KeyPair
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Configuration
@ConditionalOnProperty(name = ["jwt.mode"], havingValue = "rsa", matchIfMissing = false)
class RsaJwtConfig(
    private val keyPair: KeyPair
) {

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey

        val jwk: JWK = RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .build()

        val jwkSet = JWKSet(jwk)
        val jwkSource: JWKSource<SecurityContext> = ImmutableJWKSet(jwkSet)

        return NimbusJwtEncoder(jwkSource)
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(keyPair.public as RSAPublicKey).build()
    }
}
