package com.serejka.firstsearcher.handler

import com.serejka.firstsearcher.model.Thread
import com.serejka.firstsearcher.model.Worker
import com.serejka.firstsearcher.repository.ThreadRepository
import com.serejka.firstsearcher.repository.WorkerRepository
import com.serejka.firstsearcher.service.CollectorService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class MessageHandler(
    private val collectorService: CollectorService,
    private val workerRepository: WorkerRepository,
    private val threadRepository: ThreadRepository
) {

    fun handleMessage(update: Update?): BotApiMethod<*> {
        collectorService.collectData(update!!)

        if (update.hasMessage()) {
            val regex = Regex("^(https://)([\\w-]{1,32}\\.[\\w-]{1,32})[^\\s@]*\$")
            val text = update.message.text
            val message = SendMessage()
            val chatId = update.message.chatId
            if (regex.matches(text)) {
                println("Matched string from user - " + update.message.chat.firstName)
                if (workerRepository.existsByUser_UserIdAndAvailableThreadsGreaterThan(chatId, 0)) {
                    workerRepository.findByUser_UserId(chatId)
                        .ifPresent { worker -> threadRepository.save(createThread(worker, text)) }
                }
                message.text = "Saved link to check it"
            } else {
                message.text = text + text + text
            }

            message.chatId = chatId.toString()
            return message
        }
        return SendMessage()
    }

    fun createThread(worker: Worker, link: String): Thread {
        val thread = Thread()
        thread.link = link
        thread.worker = worker
        return thread
    }
}
