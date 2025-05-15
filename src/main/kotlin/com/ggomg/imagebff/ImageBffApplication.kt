package com.ggomg.imagebff

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class ImageBffApplication

fun main(args: Array<String>) {
    runApplication<ImageBffApplication>(*args)
}
