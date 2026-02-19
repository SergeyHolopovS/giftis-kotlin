package com.giftis.exceptions.user

import com.giftis.exceptions.basic.BasicException
import org.springframework.http.HttpStatus

class UserNotFoundException(
    override val message: String = "Пользователь не найден",
    override val status: HttpStatus = HttpStatus.NOT_FOUND,
) : BasicException(message, status)