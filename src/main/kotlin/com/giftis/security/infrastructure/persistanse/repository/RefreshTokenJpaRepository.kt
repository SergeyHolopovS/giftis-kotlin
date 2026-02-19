package com.giftis.security.infrastructure.persistanse.repository

import com.giftis.security.infrastructure.persistanse.entity.RefreshTokenJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface RefreshTokenJpaRepository : JpaRepository<RefreshTokenJpaEntity, UUID> {

    fun findByToken(token: String): Optional<RefreshTokenJpaEntity>

    fun existsByToken(token: String): Boolean

    fun findByTokenPairIdAndIsActiveTrue(tokenPairId: String): Optional<RefreshTokenJpaEntity>

}