package com.serejka.firstsearcher.controller

import com.serejka.firstsearcher.context.UpdateInterceptor
import com.serejka.firstsearcher.webhook.NotifyBot
import lombok.extern.slf4j.Slf4j
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

@Slf4j
@RestController
@RequestMapping("/")
class WebHookController(
    val updateInterceptor: UpdateInterceptor,
    val bot: NotifyBot
) {
    @PostMapping
    fun onUpdateReceived(@RequestBody update: Update): BotApiMethod<*> {
        updateInterceptor.populateFromUpdate(update)
        return bot.onWebhookUpdateReceived(update)
    }
}
