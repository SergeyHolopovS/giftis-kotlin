package com.giftis.security.domain.model

data class TokenPair(
    val authToken: String,
    val refreshToken: String,
    val tokenPairId: String
)