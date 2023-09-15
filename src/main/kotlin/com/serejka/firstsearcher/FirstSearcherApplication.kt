package com.serejka.firstsearcher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan("com.serejka")
class FirstSearcherApplication

fun main(args: Array<String>) {
    runApplication<FirstSearcherApplication>(*args)
}
