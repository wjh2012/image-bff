package com.ggomg.imagebff

import com.ggomg.imagebff.common.jwt.config.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class ImageBffApplication

fun main(args: Array<String>) {
    runApplication<ImageBffApplication>(*args)
}
