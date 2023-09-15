package com.serejka.firstsearcher.repository

import com.serejka.firstsearcher.model.Thread
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ThreadRepository : JpaRepository<Thread, Long> {
    fun findAllByActiveTrue(): List<Thread>
}
