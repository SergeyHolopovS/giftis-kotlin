package com.giftis.exceptions.link

import com.giftis.exceptions.basic.BasicException
import org.springframework.http.HttpStatus

data class YouAreNotOwnerException(
    override val message: String = "Вы не являетесь владельцем данной связи",
    override val status: HttpStatus = HttpStatus.FORBIDDEN,
) : BasicException(message, status)
