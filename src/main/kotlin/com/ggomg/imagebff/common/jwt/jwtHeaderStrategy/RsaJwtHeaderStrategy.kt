package com.ggomg.imagebff.common.jwt.jwtHeaderStrategy

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(name = ["jwt.mode"], havingValue = "rsa", matchIfMissing = false)
class RsaJwtHeaderStrategy : JwtHeaderStrategy {
    override fun getJwsHeader(): JwsHeader {
        return JwsHeader.with(SignatureAlgorithm.RS256).build()
    }
}