package com.giftis.security.infrastructure.persistanse.entity

import com.giftis.user.infrastructure.persistanse.entity.UserJpaEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.Instant
import java.util.UUID
import kotlin.time.ExperimentalTime

@Entity
data class RefreshTokenJpaEntity @OptIn(ExperimentalTime::class) constructor(

    // Id токена
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    // Сам токен
    @Column(nullable = false, unique = true, columnDefinition = "text")
    var token: String,

    // Владелец токена
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserJpaEntity,

    // Id пары токенов
    @Column(nullable = false)
    val tokenPairId: String,

    // Активен ли
    @Column(nullable = false)
    val isActive: Boolean = true,

    // Время создания
    @CreationTimestamp
    val createdAt: Instant? = null,

    // Время истечения срока годности
    @Column(nullable = false)
    val expiresAt: Instant,

)