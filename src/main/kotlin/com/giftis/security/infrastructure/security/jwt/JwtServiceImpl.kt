package com.giftis.security.infrastructure.security.jwt

import com.giftis.configs.JwtConfig
import com.giftis.exceptions.auth.TokenUnauthorizedException
import com.giftis.security.application.service.JwtService
import com.giftis.security.domain.model.TokenPair
import com.giftis.user.domain.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.security.PrivateKey
import java.security.PublicKey
import java.util.*
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

    override fun extractTokenPairId(token: String, ignoreExpiration: Boolean): String? {
        return try {
            extractClaim(
                token=token,
                ignoreExpiration=ignoreExpiration,
                claimsResolver = { claims ->
                    claims?.get(jwtConfig.tokenPairIdClaim) as? String
                }
            )
        } catch (_: Exception) {
            null
        }
    }

    override fun extractId(token: String, ignoreExpiration: Boolean): String
        = validateAndGetClaims(token, ignoreExpiration).subject

    override fun isRefreshTokenValid(token: String, user: User): Boolean {
        try {
            val claims = validateAndGetClaims(token)

            val tokenType = claims.get<String?>(jwtConfig.refreshTokenType, String::class.java)
            if (jwtConfig.refreshTokenType != tokenType) {
                return false
            }

            val tokenId = claims.subject
            return !(user.id != tokenId || claims.expiration.before(Date()))
        } catch (e: java.lang.Exception) {
            logger.error("Refresh token validation failed: {}", e.message)
            return false
        }
    }

    override fun isAccessTokenExpired(token: String): Boolean {
        try {
            val claims: Claims = Jwts
                .parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJwt(token)
                .body

            return claims.expiration.before(Date())
        } catch (_: ExpiredJwtException) {
            return true
        } catch (e: JwtException) {
            logger.error("Invalid token during expiration check: {}", e.message)
            return true
        }
    }

    private fun <T> extractClaim(token: String?, claimsResolver: Function<Claims?, T?>, ignoreExpiration: Boolean = false): T? {
        val claims: Claims = validateAndGetClaims(token, ignoreExpiration)
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

    private fun validateAndGetClaims(token: String?, ignoreExpiration: Boolean = false): Claims {
        try {
            return Jwts
                .parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJwt(token)
                .body
        } catch (e: ExpiredJwtException) {
            if (ignoreExpiration) return e.claims
            throw TokenUnauthorizedException("Токен устарел")
        } catch (e: JwtException) {
            logger.error("Failed to validate token: ${e.message}")
            throw TokenUnauthorizedException("Токен невалиден")
        }
    }

}