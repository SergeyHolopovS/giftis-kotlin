package com.giftis.security.domain.repository

import com.giftis.security.domain.model.RefreshToken
import com.giftis.user.domain.model.User
import java.time.Instant

interface RefreshTokenRepository {

    fun existsByToken(token: String): Boolean

    fun save(tokenPairId: String, token: String, user: User): RefreshToken

    fun isRefreshTokenActiveByTokenPairId(tokenPairId: String): Boolean

    fun isRefreshTokenActiveInDatabase(token: String): Boolean

    fun deactivateToken(token: String)

    fun deleteExpiredTokens()

}