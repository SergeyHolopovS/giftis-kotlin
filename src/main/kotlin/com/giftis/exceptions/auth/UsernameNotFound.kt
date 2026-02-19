package com.giftis.exceptions.auth

import com.giftis.exceptions.basic.BasicException
import org.springframework.http.HttpStatus

class UsernameNotFound(
    override val message: String = "Юзернейм скрыт или отсутствует",
    override val status: HttpStatus = HttpStatus.BAD_REQUEST
) : BasicException(message, status)