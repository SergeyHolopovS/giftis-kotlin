package com.giftis.security.infrastructure.persistence.scheduler

import com.giftis.security.domain.repository.RefreshTokenRepository
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class AutoDeleteTokens(
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    private val logger = KotlinLogging.logger {}

    @Scheduled(
        fixedRateString = $$"${jwt.schedule.refresh-autodelete-rate}",
        timeUnit = TimeUnit.DAYS
    )
    fun run() {
        refreshTokenRepository.deleteExpiredTokens()
        logger.info("Старые токены обновления удалены")
    }

}