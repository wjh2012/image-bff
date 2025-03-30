package com.ggomg.imagebff.common.jwt.config

import com.nimbusds.jose.jwk.source.ImmutableSecret
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.nio.charset.StandardCharsets
import javax.crypto.spec.SecretKeySpec


@Configuration
@ConditionalOnProperty(name = ["jwt.mode"], havingValue = "hmac", matchIfMissing = true)
class HmacJwtConfig(private val jwtProps: JwtProperties) {

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val secretKey =
            SecretKeySpec(jwtProps.hmac.secret.toByteArray(StandardCharsets.UTF_8), "HmacSHA256")
        val jwkSource = ImmutableSecret<SecurityContext>(secretKey)
        return NimbusJwtEncoder(jwkSource)
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val secretKey =
            SecretKeySpec(jwtProps.hmac.secret.toByteArray(StandardCharsets.UTF_8), "HmacSHA256")
        return NimbusJwtDecoder.withSecretKey(secretKey).build()
    }
}