package com.giftis.exceptions.auth

import com.giftis.exceptions.basic.BasicException
import org.springframework.http.HttpStatus

data class TelegramParseException(
    override val message: String = "Ошибка авторизации Telegram",
    override val status: HttpStatus = HttpStatus.BAD_REQUEST,
) : BasicException(message, status)
