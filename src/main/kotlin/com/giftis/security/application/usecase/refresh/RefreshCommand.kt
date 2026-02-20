package com.giftis.security.application.usecase.refresh

data class RefreshCommand(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
)
