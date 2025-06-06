package com.ggomg.imagebff.auth.security.jwt.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import java.security.KeyFactory
import java.security.KeyPair
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Configuration
@ConditionalOnProperty(name = ["jwt.mode"], havingValue = "rsa", matchIfMissing = false)
class ProdRsaKeyConfig(
    private val jwtProps: JwtProperties
) {

    @Bean
    fun keyPair(): KeyPair {
        val publicKey = loadPublicKey(ClassPathResource(jwtProps.rsa.publicKeyPath))
        val privateKey = loadPrivateKey(ClassPathResource(jwtProps.rsa.privateKeyPath))
        return KeyPair(publicKey, privateKey)
    }

    private fun loadPublicKey(resource: Resource): RSAPublicKey {
        val keyContent = resource.inputStream.bufferedReader().use { it.readText() }
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s".toRegex(), "") // 공백 및 줄바꿈 제거

        val decoded = Base64.getDecoder().decode(keyContent)
        val keySpec = X509EncodedKeySpec(decoded)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec) as RSAPublicKey
    }

    private fun loadPrivateKey(resource: Resource): RSAPrivateKey {
        val keyContent = resource.inputStream.bufferedReader().use { it.readText() }
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\s".toRegex(), "")

        val decoded = Base64.getDecoder().decode(keyContent)
        val keySpec = PKCS8EncodedKeySpec(decoded)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }

}