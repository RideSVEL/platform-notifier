package com.serejka.firstsearcher.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "telegram.notifier-bot")
data class TelegramProperties(
    val username: String,
    val token: String,
    val webHookPath: String
)
