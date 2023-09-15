package com.serejka.firstsearcher.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "threads")
open class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: UUID? = null

    @Column(length = 5000)
    open var link: String? = null
    open var active: Boolean? = true
    open var name: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    open var worker: Worker? = null
}
