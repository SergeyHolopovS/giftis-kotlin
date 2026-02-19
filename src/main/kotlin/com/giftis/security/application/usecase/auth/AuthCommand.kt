package com.giftis.security.application.usecase.auth

import com.giftis.security.application.models.TelegramUser

data class AuthCommand(
    val telegramUser: TelegramUser,
)
