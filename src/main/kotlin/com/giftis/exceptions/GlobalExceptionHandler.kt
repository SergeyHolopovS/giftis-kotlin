package com.giftis.exceptions

import com.giftis.exceptions.basic.BasicException
import com.giftis.exceptions.dto.ErrorDto
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.resource.NoResourceFoundException

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(BasicException::class)
    fun handleBusinessException(e: BasicException): ResponseEntity<ErrorDto> {
        return ResponseEntity
            .status(e.status)
            .body(
                ErrorDto(
                    e.message,
                    e.status,
                )
            )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleNoBody(): ResponseEntity<ErrorDto> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorDto(
                    "Тело запроса отсутствует",
                    HttpStatus.BAD_REQUEST
                )
            )
    }

    @ExceptionHandler(NoResourceFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(): ResponseEntity<ErrorDto> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorDto(
                    "Путь не найден",
                    HttpStatus.NOT_FOUND
                )
            )
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInternalError(e: Exception): ResponseEntity<ErrorDto> {
        logger.error(e) { "Internal server error\n${e.message}" }
        return ResponseEntity
            .internalServerError()
            .body(
                ErrorDto(
                    "Внутренняя ошибка сервера",
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            )
    }

}