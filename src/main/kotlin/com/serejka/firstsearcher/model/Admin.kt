package com.serejka.firstsearcher.model

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "admin")
open class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    open val user: User? = null
}
