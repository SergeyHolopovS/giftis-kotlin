package com.giftis.security.application.usecase.refresh

data class RefreshResult(
    val accessToken: String,
    val refreshToken: String,
)
