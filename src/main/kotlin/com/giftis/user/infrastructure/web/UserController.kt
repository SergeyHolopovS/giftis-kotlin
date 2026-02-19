package com.giftis.user.infrastructure.web

import com.giftis.security.infrastructure.mappers.TelegramWebAppMapper
import com.giftis.user.application.usecase.registration.RegistrationCommand
import com.giftis.user.application.usecase.registration.RegistrationUseCase
import com.giftis.user.infrastructure.web.requests.RegistrationRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/user")
class UserController(
    val telegramWebAppMapper: TelegramWebAppMapper,
    val registrationUseCase: RegistrationUseCase,
) {

    @PostMapping
    fun registration(
        @RequestBody body: RegistrationRequest
    ): ResponseEntity<Unit> {
        val telegramUser = telegramWebAppMapper.parseInitData(body.initData)
        registrationUseCase.execute(
            RegistrationCommand(
                telegramUser
            )
        )
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

}