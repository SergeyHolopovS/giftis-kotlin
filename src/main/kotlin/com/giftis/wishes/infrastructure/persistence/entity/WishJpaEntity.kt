package com.giftis.wishes.infrastructure.persistence.entity

import com.giftis.user.infrastructure.persistence.entity.UserJpaEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.UUID

@Entity
data class WishJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserJpaEntity,

    val title: String,

    val note: String? = null,

    val link: String? = null,

)
