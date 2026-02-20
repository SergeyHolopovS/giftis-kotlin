package com.giftis.security.infrastructure.web.cookie

import com.giftis.configs.CookieConfig
import com.giftis.exceptions.auth.TokenUnauthorizedException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CookieService(
    private val cookieConfig: CookieConfig,
    @Value($$"${spring.profiles.active}")
    private val profile: String,
    @Value($$"${prod.domain}")
    private val domain: String,
) {

    fun addCookie(key: String, value: String, response: HttpServletResponse) {
        val cookie: ResponseCookie = ResponseCookie
            .from(key, value)
            .domain(if (profile == "dev") "localhost" else domain)
            .httpOnly(true)
            .secure(true)
            .maxAge(
                Duration.ofSeconds(cookieConfig.cookieLifetime),
            )
            .sameSite(if (profile == "dev") "None" else "Lax")
            .path("/")
            .build()
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())
    }

    fun clearCookie(key: String, response: HttpServletResponse) {
        val cookie: ResponseCookie = ResponseCookie
            .from(key, "")
            .domain(if (profile == "dev") "localhost" else domain)
            .httpOnly(true)
            .secure(true)
            .maxAge(Duration.ZERO)
            .sameSite(if (profile == "dev") "None" else "Lax")
            .path("/")
            .build()
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())
    }

    fun extractAccessTokenFromCookie(request: HttpServletRequest): String {
        return request.cookies
            ?.firstOrNull { it.name == cookieConfig.accessTokenName }
            ?.value
            ?: throw TokenUnauthorizedException("Токен авторизации не найден")
    }

    fun extractRefreshTokenFromCookie(request: HttpServletRequest): String {
        return request.cookies
            ?.firstOrNull { it.name == cookieConfig.refreshTokenName }
            ?.value
            ?: throw TokenUnauthorizedException("Токен обновления не найден")
    }

}