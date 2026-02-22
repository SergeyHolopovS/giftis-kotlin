package com.giftis.exceptions.wish

import com.giftis.exceptions.basic.BasicException
import org.springframework.http.HttpStatus

data class YouAreNotOwnerException(
    override val message: String = "Вы не являетесь владельцем данной хотелки",
    override val status: HttpStatus = HttpStatus.FORBIDDEN,
) : BasicException(message, status)
