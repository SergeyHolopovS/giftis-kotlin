package com.giftis.security.infrastructure.persistence.repository

import com.giftis.security.infrastructure.persistence.entity.RefreshTokenJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface RefreshTokenJpaRepository : JpaRepository<RefreshTokenJpaEntity, UUID> {

    fun existsByToken(token: String): Boolean

    fun findByTokenPairIdAndIsActiveTrue(tokenPairId: String): Optional<RefreshTokenJpaEntity>

    fun findByTokenAndIsActiveTrue(tokenPairId: String): Optional<RefreshTokenJpaEntity>

    fun deleteByToken(token: String)

    fun deleteByExpiresAtBefore(dateTime: Instant?)

}