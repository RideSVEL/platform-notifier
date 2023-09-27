package com.serejka.firstsearcher.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("redisson")
data class RedissonProperties(
        val url: String,
        val password: String
)
