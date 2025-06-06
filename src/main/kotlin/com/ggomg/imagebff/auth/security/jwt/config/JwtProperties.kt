package com.ggomg.imagebff.auth.security.jwt.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtProperties {
    var mode: String = "hmac"
    var expiration: Long = 0L

    var rsa: Rsa = Rsa()
    var hmac: Hmac = Hmac()

    class Rsa {
        var publicKeyPath: String = ""
        var privateKeyPath: String = ""
    }

    class Hmac {
        var secret: String = ""
    }
}