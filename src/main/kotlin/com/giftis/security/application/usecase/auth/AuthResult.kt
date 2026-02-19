package com.giftis.security.application.usecase.auth

data class AuthResult (
    val accessToken: String,
    val refreshToken: String,
)