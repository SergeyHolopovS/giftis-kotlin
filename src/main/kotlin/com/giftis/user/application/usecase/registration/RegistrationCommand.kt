package com.giftis.user.application.usecase.registration

import com.giftis.security.application.models.TelegramUser

data class RegistrationCommand(
    val telegramUser: TelegramUser,
)
