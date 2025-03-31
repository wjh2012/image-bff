package com.ggomg.imagebff

import com.ggomg.imagebff.common.jwt.config.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class ImageBffApplication

fun main(args: Array<String>) {
    runApplication<ImageBffApplication>(*args)
}
