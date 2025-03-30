package com.ggomg.imagebff.common.jwt.jwtHeaderStrategy

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(name = ["jwt.mode"], havingValue = "hmac", matchIfMissing = true)
class HmacJwtHeaderStrategy : JwtHeaderStrategy {
    override fun getJwsHeader(): JwsHeader {
        return JwsHeader.with(MacAlgorithm.HS256).build()
    }
}