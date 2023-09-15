package com.serejka.firstsearcher.repository

import com.serejka.firstsearcher.model.Admin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminRepository : JpaRepository<Admin, Long> {
    fun existsAdminByUser_UserId(userId: Long): Boolean
}
