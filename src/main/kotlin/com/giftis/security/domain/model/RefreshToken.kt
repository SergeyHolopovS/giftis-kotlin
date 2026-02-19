package com.giftis.security.domain.model

import com.giftis.user.domain.model.User
import java.time.Instant
import java.util.UUID
import kotlin.time.ExperimentalTime

data class RefreshToken @OptIn(ExperimentalTime::class) constructor(
    val id: UUID,
    val token: String,
    val user: User,
    val tokenPairId: String,
    val isActive: Boolean,
    val createdAt: Instant,
    val expiresAt: Instant
)