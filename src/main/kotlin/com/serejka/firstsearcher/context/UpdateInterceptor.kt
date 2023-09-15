package com.serejka.firstsearcher.context

import com.serejka.firstsearcher.repository.AdminRepository
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class UpdateInterceptor(private val adminRepository: AdminRepository) {

    fun populateFromUpdate(update: Update) {
        BotContext.userId.set(update.message.chatId)
        BotContext.isAdmin.set(
            adminRepository.existsAdminByUser_UserId(BotContext.userId.get())
        )
    }
}
