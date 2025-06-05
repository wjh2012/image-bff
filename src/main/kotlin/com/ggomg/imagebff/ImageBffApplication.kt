package com.ggomg.imagebff

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableJpaAuditing
@SpringBootApplication
@EnableWebSecurity(debug = true)
class ImageBffApplication

fun main(args: Array<String>) {
    runApplication<ImageBffApplication>(*args)
}
