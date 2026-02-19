package com.giftis.security.infrastructure.persistanse.repository

import com.giftis.configs.JwtConfig
import com.giftis.security.domain.model.RefreshToken
import com.giftis.security.domain.repository.RefreshTokenRepository
import com.giftis.security.infrastructure.persistanse.entity.RefreshTokenJpaEntity
import com.giftis.security.infrastructure.persistanse.mapper.RefreshTokenMapper
import com.giftis.user.domain.model.User
import com.giftis.user.infrastructure.persistanse.mapper.UserMapper
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.ExperimentalTime

@Repository
class RefreshTokenRepositoryImpl(
    private val mapper: RefreshTokenMapper,
    private val userMapper: UserMapper,
    private val repository: RefreshTokenJpaRepository,
    private val jwtConfig: JwtConfig
) : RefreshTokenRepository {

    override fun existsByToken(token: String): Boolean = repository.existsByToken(token)

    @OptIn(ExperimentalTime::class)
    override fun save(
        tokenPairId: String,
        token: String,
        user: User
    ): RefreshToken {
        return mapper.toModel(
            repository.save(
                RefreshTokenJpaEntity(
                    token = token,
                    tokenPairId = tokenPairId,
                    user = userMapper.toEntity(user),
                    expiresAt = Instant.now().plus(
                        jwtConfig.refreshTokenExpiration,
                        ChronoUnit.MILLIS
                    )
                )
            )
        )
    }

    override fun isRefreshTokenActiveByTokenPairId(tokenPairId: String): Boolean {
        return repository.findByTokenPairIdAndIsActiveTrue(tokenPairId).isPresent
    }

}