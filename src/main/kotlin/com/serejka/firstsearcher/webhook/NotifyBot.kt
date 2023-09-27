package com.serejka.firstsearcher.webhook

import com.serejka.firstsearcher.config.properties.TelegramProperties
import com.serejka.firstsearcher.handler.MessageHandler
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class NotifyBot(val properties: TelegramProperties,
                val handler: MessageHandler) : TelegramWebhookBot(properties.token) {

    override fun getBotUsername(): String {
        return properties.username
    }

    override fun onWebhookUpdateReceived(update: Update?): BotApiMethod<*> {
        return handler.handleMessage(update)
    }

    override fun getBotPath(): String {
        return properties.webHookPath
    }
}
