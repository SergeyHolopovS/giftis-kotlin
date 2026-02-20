package com.giftis.security.application.service

import com.giftis.security.domain.model.TokenPair
import com.giftis.user.domain.model.User
import org.springframework.stereotype.Service

@Service
interface  JwtService {

    fun generateTokenPair(user: User): TokenPair

    fun getId(token: String): String

    fun isTokenValid(token: String, user: User): Boolean

    fun extractTokenPairId(token: String, ignoreExpiration: Boolean = false): String?

    fun extractId(token: String, ignoreExpiration: Boolean = false): String

    fun isRefreshTokenValid(token: String, user: User): Boolean

    fun isAccessTokenExpired(token: String): Boolean

}