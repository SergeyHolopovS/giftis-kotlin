package com.giftis.exceptions.user

import com.giftis.exceptions.basic.BasicException
import org.springframework.http.HttpStatus

data class UserAlreadyExistsException(
    override val message: String = "Такой пользователь уже существует",
    override val status: HttpStatus = HttpStatus.BAD_REQUEST
) : BasicException(message, status)
