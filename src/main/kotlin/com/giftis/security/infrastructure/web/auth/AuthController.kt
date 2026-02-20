package com.giftis.security.infrastructure.web.auth

import com.giftis.configs.CookieConfig
import com.giftis.security.application.models.TelegramUser
import com.giftis.security.application.usecase.auth.AuthCommand
import com.giftis.security.application.usecase.auth.AuthUserUseCase
import com.giftis.security.application.usecase.refresh.RefreshCommand
import com.giftis.security.application.usecase.refresh.RefreshUseCase
import com.giftis.security.infrastructure.mappers.TelegramWebAppMapper
import com.giftis.security.infrastructure.web.auth.requests.AuthRequest
import com.giftis.security.infrastructure.web.cookie.CookieService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val telegramMapper: TelegramWebAppMapper,
    private val authUserUseCase: AuthUserUseCase,
    private val refreshUseCase: RefreshUseCase,
    private val cookieService: CookieService,
    private val cookieConfig: CookieConfig,
) {

    @PostMapping
    fun authorize(
        @RequestBody requestBody: AuthRequest,
        servletResponse: HttpServletResponse,
    ): ResponseEntity<Unit> {
        val user: TelegramUser = telegramMapper.parseInitData(requestBody.initData)
        val result = authUserUseCase.execute(
            AuthCommand(
                user
            )
        )
        cookieService.addCookie(
            cookieConfig.accessTokenName,
            result.accessToken,
            servletResponse
        )
        cookieService.addCookie(
            cookieConfig.refreshTokenName,
            result.refreshToken,
            servletResponse
        )
        return ResponseEntity.ok().build()
    }

    @PostMapping("/refresh")
    fun refresh(
        servletResponse: HttpServletResponse,
        servletRequest: HttpServletRequest,
        @AuthenticationPrincipal userId: String,
    ) {
        refreshUseCase.execute(
            RefreshCommand(
                accessToken = cookieService.extractAccessTokenFromCookie(servletRequest),
                refreshToken = cookieService.extractRefreshTokenFromCookie(servletRequest),
                userId = userId
            )
        )
    }

    @PostMapping("/logout")
    fun logout(servletResponse: HttpServletResponse): ResponseEntity<Unit> {
        cookieService.clearCookie(
            cookieConfig.accessTokenName,
            servletResponse
        )
        cookieService.clearCookie(
            cookieConfig.refreshTokenName,
            servletResponse
        )
        return ResponseEntity.ok().build()
    }

}