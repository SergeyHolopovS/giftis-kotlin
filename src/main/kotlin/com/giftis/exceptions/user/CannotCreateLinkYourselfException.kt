package com.giftis.exceptions.user

import com.giftis.exceptions.basic.BasicException
import org.springframework.http.HttpStatus

data class CannotCreateLinkYourselfException(
    override val message: String = "Невозможно создать связь со самим собой",
    override val status: HttpStatus = HttpStatus.BAD_REQUEST,
) : BasicException(message, status)