package com.giftis.exceptions.link

import com.giftis.exceptions.basic.BasicException
import org.springframework.http.HttpStatus

data class LinkNotFound(
    override val message: String = "Связь не найдена",
    override val status: HttpStatus = HttpStatus.NOT_FOUND,
) : BasicException(message, status)
