package com.serejka.firstsearcher.service

import com.serejka.firstsearcher.model.User
import com.serejka.firstsearcher.model.Worker
import com.serejka.firstsearcher.repository.UserRepository
import com.serejka.firstsearcher.repository.WorkerRepository
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class CollectorService(
    private val userRepository: UserRepository,
    private val workerRepository: WorkerRepository
) {

    fun collectData(update: Update) {
        collectUserInfo(update.message)
    }

    private fun collectUserInfo(message: Message) {
        val findByUserId = userRepository.findByUserId(message.chatId)
        if (findByUserId.isEmpty) {
            val user = User()
            user.userName = message.chat.userName
            user.firstName = message.chat.firstName
            user.lastName = message.chat.lastName
            user.userId = message.chat.id
            userRepository.save(user)

            workerRepository.save(createWorker(user))
        }
    }

    private fun createWorker(user: User): Worker {
        val worker = Worker()
        worker.user = user
        return worker
    }
}
