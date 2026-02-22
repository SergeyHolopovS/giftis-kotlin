package com.giftis.security.application.usecase.refresh

import com.giftis.exceptions.auth.TokenUnauthorizedException
import com.giftis.security.application.service.JwtService
import com.giftis.security.domain.repository.RefreshTokenRepository
import com.giftis.user.domain.model.User
import com.giftis.user.domain.repository.UserRepository
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class RefreshUseCase(
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository
) {

    private val logger = KotlinLogging.logger {}

    fun execute(command: RefreshCommand): RefreshResult {
        val user: User = userRepository.findById(command.userId)

        if (!jwtService.isRefreshTokenValid(command.accessToken, user))
            throw TokenUnauthorizedException("Токен обновления невалиден")

        if (!jwtService.isAccessTokenExpired(command.accessToken))
            throw TokenUnauthorizedException("Токен доступа всё ещё активен, дождитесь истечения срока годности")

        val accessTokenId = jwtService.extractId(command.refreshToken, true)
        if (user.id != accessTokenId)
            throw TokenUnauthorizedException("В токене доступа не найден TelegramId")

        val accessTokenPairId = jwtService.extractTokenPairId(command.refreshToken, true)
        val refreshTokenPairId = jwtService.extractTokenPairId(command.refreshToken)
        if (
            accessTokenPairId == null ||
            refreshTokenPairId == null ||
            accessTokenPairId != refreshTokenPairId
        ) {
            logger.warn("Token pair mismatch for user with id: ${user.id}")
            throw TokenUnauthorizedException("Токены доступа и обновления не связаны")
        }

        if (!refreshTokenRepository.isRefreshTokenActiveInDatabase(refreshTokenPairId))
            throw TokenUnauthorizedException("Токен обновления не активен")

        refreshTokenRepository.deactivateToken(refreshTokenPairId)

        val tokenPair = jwtService.generateTokenPair(user)

        refreshTokenRepository.save(
            tokenPairId = tokenPair.tokenPairId,
            token = tokenPair.refreshToken,
            user = user,
        )

        return RefreshResult(
            accessToken = tokenPair.authToken,
            refreshToken = tokenPair.refreshToken,
        )
    }

}