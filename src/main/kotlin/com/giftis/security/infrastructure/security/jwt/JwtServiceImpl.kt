package com.giftis.security.infrastructure.security.jwt

import com.giftis.configs.JwtConfig
import com.giftis.exceptions.auth.TokenUnauthorizedException
import com.giftis.security.application.service.JwtService
import com.giftis.security.domain.model.TokenPair
import com.giftis.user.domain.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.security.PrivateKey
import java.security.PublicKey
import java.util.Date
import java.util.UUID
import java.util.function.Function

@Component
class JwtServiceImpl(
    private val publicKey: PublicKey,
    private val privateKey: PrivateKey,
    private val jwtConfig: JwtConfig
) : JwtService {

    private val logger = KotlinLogging.logger {}

    override fun generateTokenPair(user: User): TokenPair {
        val tokenPairId = UUID.randomUUID().toString()

        val accessToken = generateAccessToken(user, tokenPairId)
        val refreshToken = generateRefreshToken(user, tokenPairId)

        return TokenPair(accessToken, refreshToken, tokenPairId)
    }

    override fun getId(token: String): String = validateAndGetClaims(token).subject

    override fun isTokenValid(token: String, user: User): Boolean {
        try {
            val claims = validateAndGetClaims(token)

            val tokenId = claims.subject
            if (user.id != tokenId) {
                return false
            }

            val tokenType = claims.get(jwtConfig.tokenTypeClaim, String::class.java)
            if (jwtConfig.accessTokenType != tokenType) {
                return false
            }

            return !claims.expiration.before(Date())
        } catch (e: Exception) {
            logger.error("Token validation failed: {}", e.message)
            return false
        }
    }

    override fun extractTokenPairId(token: String): String? {
        return try {
            extractClaim(token) { claims ->
                claims?.get(jwtConfig.tokenPairIdClaim) as? String
            }
        } catch (e: java.lang.Exception) {
            null
        }
    }

    override fun extractId(token: String): String = validateAndGetClaims(token).subject

    private fun <T> extractClaim(token: String?, claimsResolver: Function<Claims?, T?>): T? {
        val claims: Claims = validateAndGetClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun generateAccessToken(user: User, tokenPairId: String): String {
        val claims: MutableMap<String?, Any?> = HashMap()
        claims[jwtConfig.tokenPairIdClaim] = tokenPairId
        claims[jwtConfig.tokenTypeClaim] = jwtConfig.accessTokenType

        return generateToken(claims, user, jwtConfig.accessTokenExpiration)
    }

    private fun generateRefreshToken(user: User, tokenPairId: String): String {
        val claims: MutableMap<String?, Any?> = HashMap()
        claims[jwtConfig.tokenPairIdClaim] = tokenPairId
        claims[jwtConfig.tokenTypeClaim] = jwtConfig.refreshTokenType

        return generateToken(claims, user, jwtConfig.refreshTokenExpiration)
    }

    private fun generateToken(
        extraClaims: MutableMap<String?, Any?>?,
        user: User,
        expiration: Long
    ): String {
        return Jwts
            .builder()
            .signWith(privateKey)
            .addClaims(extraClaims)
            .setSubject(user.id)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration * 1000)) // переводится в секундах, т.е. указывать значения в секундах
            .compact()
    }

    private fun validateAndGetClaims(token: String?): Claims {
        try {
            return Jwts
                .parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJwt(token)
                .body
        } catch (e: JwtException) {
            logger.error("Failed to validate token: ${e.message}")
            throw TokenUnauthorizedException("Токен невалиден")
        }
    }

}