package com.serejka.firstsearcher.repository

import com.serejka.firstsearcher.model.Worker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface WorkerRepository : JpaRepository<Worker, Long> {
    fun existsByUser_UserIdAndAvailableThreadsGreaterThan(userId: Long, availableThreads: Int): Boolean
    fun findByUser_UserId(userId: Long): Optional<Worker>
    fun findById(id: UUID): Optional<Worker>
}
