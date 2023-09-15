package com.serejka.firstsearcher.model

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "workers")
open class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: UUID? = null
    open var maxThreads: Int? = 0
    open var availableThreads: Int? = 0

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    open var user: User? = null
}
