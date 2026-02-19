package com.giftis.exceptions.auth

import com.giftis.exceptions.basic.BasicException
import org.springframework.http.HttpStatus

class TokenUnauthorizedException(
    override val message: String = "Ошибка авторизации",
    override val status: HttpStatus = HttpStatus.BAD_REQUEST,
) : BasicException(message, status)