package com.serejka.firstsearcher.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "users")
open class User {
    @Id
    @Column(nullable = false, unique = true)
    open var userId: Long? = null
    open var userName: String? = null
    open var firstName: String? = null
    open var lastName: String? = null
    open var countOfUse = 0
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    open var worker: Worker? = null
}
