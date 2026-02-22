package com.giftis.exceptions.wish

import com.giftis.exceptions.basic.BasicException
import org.springframework.http.HttpStatus

data class SuchUrlAlreadyExists(
    override val message: String = "Хотелка с таким URL уже существует",
    override val status: HttpStatus = HttpStatus.CONFLICT
) : BasicException(message, status)
