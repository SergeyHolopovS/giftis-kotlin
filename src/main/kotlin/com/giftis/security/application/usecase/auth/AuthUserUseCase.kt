package com.giftis.security.application.usecase.auth

import com.giftis.exceptions.auth.UsernameNotFound
import com.giftis.security.application.service.JwtService
import com.giftis.security.domain.repository.RefreshTokenRepository
import com.giftis.user.domain.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class AuthUserUseCase(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtService: JwtService,
) {

    fun execute(command: AuthCommand): AuthResult {
        if (command.telegramUser.username == null) throw UsernameNotFound()
        val user = userRepository.findById(command.telegramUser.id.toString())

        val tokenPair = jwtService.generateTokenPair(user)

        refreshTokenRepository.save(
            tokenPairId = tokenPair.tokenPairId,
            token = tokenPair.refreshToken,
            user = user,
        )

        return AuthResult(
            accessToken = tokenPair.authToken,
            refreshToken = tokenPair.refreshToken,
        )
    }

}