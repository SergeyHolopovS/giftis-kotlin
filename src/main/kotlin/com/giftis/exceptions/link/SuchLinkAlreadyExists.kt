package com.giftis.exceptions.link

import com.giftis.exceptions.basic.BasicException
import org.springframework.http.HttpStatus

data class SuchLinkAlreadyExists(
    override val message: String = "Такая связь уже существует",
    override val status: HttpStatus = HttpStatus.BAD_REQUEST,
) : BasicException(message, status)