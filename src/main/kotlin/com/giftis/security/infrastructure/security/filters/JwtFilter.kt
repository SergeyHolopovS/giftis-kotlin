package com.giftis.security.infrastructure.security.filters

import com.giftis.configs.CookieConfig
import com.giftis.security.application.service.JwtService
import com.giftis.security.domain.repository.RefreshTokenRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.Arrays
import java.util.Collections

@Component
class JwtFilter(
    private val jwtService: JwtService,
    private val cookieConfig: CookieConfig,
    private val refreshTokenRepository: RefreshTokenRepository,
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            // Если куков нет или контекст уже установлен -> идём дальше по фильтрам
            if (
                request.cookies == null ||
                SecurityContextHolder.getContext().authentication != null
            ) throw RuntimeException()

            // Вытаскиваем токен из куки
            // Если токена нет -> идём дальше по фильтрам
            val token = extractTokenFromCookie(request) ?: throw RuntimeException()

            // Вытаскиваем из токена id пары
            // Если tokenPairId == null -> access токен expired
            val tokenPairId: String = jwtService.extractTokenPairId(token) ?: throw RuntimeException()

            // Если токен неактивен == пользователь забанен или заблокировал сессию
            if (!refreshTokenRepository.isRefreshTokenActiveByTokenPairId(tokenPairId)) throw RuntimeException()

            // Вытаскиваем из jwt id
            val id: String = jwtService.extractId(token)

            // Создаём токен для установки авторизации в контекст холдере
            val authenticationToken = UsernamePasswordAuthenticationToken(
                id,
                null,
                Collections.emptyList()
            )

            // Устанавливаем авторизацию в контекст
            SecurityContextHolder.getContext().authentication = authenticationToken
        } catch (_: Exception) {}

        // Прокидываем фильтры дальше
        filterChain.doFilter(request, response)
    }

    private fun extractTokenFromCookie(request: HttpServletRequest): String? {
        // Получаем куку с access токеном
        val cookie: Cookie? = Arrays
            .stream(request.cookies)
            .filter { el ->
                el?.name.equals(
                    cookieConfig.accessTokenName
                )
            }
            .findAny()
            .orElse(null)
        return cookie?.value
    }

}