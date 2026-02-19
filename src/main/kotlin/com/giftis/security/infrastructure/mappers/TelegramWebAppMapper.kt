package com.giftis.security.infrastructure.mappers

import com.fasterxml.jackson.core.JsonProcessingException
import com.giftis.exceptions.auth.TokenUnauthorizedException
import com.giftis.exceptions.user.UserNotFoundException
import com.giftis.security.application.models.TelegramUser
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Component
class TelegramWebAppMapper(
    private val mapper: ObjectMapper,
    @Value($$"${telegram.token}")
    private val token: String,
    @Value($$"${telegram.maxAuthAge}")
    private val maxAuthAge: Long
) {

    private val hmacAlgorithm = "HmacSHA256"
    private val webAppData = "WebAppData"

    @OptIn(ExperimentalTime::class)
    fun parseInitData(initData: String): TelegramUser {
        val data = parseQueryString(initData).toMutableMap()

        // Удаляем hash из мапа, записывая его в переменную
        val receivedHash = data.remove("hash") ?: throw TokenUnauthorizedException()

        // Получаем User, чтобы запарсить если пройдём валидацию
        val userEncoded = URLDecoder.decode(
            data["user"] ?: throw UserNotFoundException(),
            StandardCharsets.UTF_8
        )

        // Проверка auth_date
        val authDateStr = data["auth_date"] ?: throw TokenUnauthorizedException()

        // Проверка на слишком старую дату авторизации
        val authDate = authDateStr.toLong()
        val now = Clock.System.now().epochSeconds
        if (now - authDate > maxAuthAge) throw TokenUnauthorizedException()

        // Собираем Map в строку, соединяя все элементы \n, а key и value через =
        val dataCheckString = data.toSortedMap().entries.joinToString("\n") { (key, value) ->
            "$key=${URLDecoder.decode(value, StandardCharsets.UTF_8)}"
        }

        // Шифруем секретный ключ
        val secretKey = hmacSha256(
            token.toByteArray(StandardCharsets.UTF_8),
            webAppData.toByteArray(StandardCharsets.UTF_8)
        )

        // Шифруем dataCheckString по secretKey
        val calculatedHash = hmacSha256(dataCheckString.toByteArray(StandardCharsets.UTF_8), secretKey)

        // Сравниваем вычисленный хэш с receivedHash
        return if (MessageDigest.isEqual(calculatedHash, hexToBytes(receivedHash))) {
            try {
                mapper.readValue(userEncoded, TelegramUser::class.java)
            } catch (e: JsonProcessingException) {
                println("Ошибка парса юзера\n${e.message}")
                throw TokenUnauthorizedException()
            }
        } else {
            throw TokenUnauthorizedException()
        }
    }

    private fun hmacSha256(data: ByteArray, key: ByteArray): ByteArray {
        return try {
            val mac = Mac.getInstance(hmacAlgorithm)
            mac.init(SecretKeySpec(key, hmacAlgorithm))
            mac.doFinal(data)
        } catch (e: Exception) {
            throw RuntimeException("HMAC calculation failed", e)
        }
    }

    private fun parseQueryString(query: String): Map<String, String> {
        return query.split("&").mapNotNull { pair ->
            val idx = pair.indexOf("=")
            if (idx > 0) {
                val key = pair.substring(0, idx)
                val value = pair.substring(idx + 1)
                key to value
            } else null
        }.toMap()
    }

    private fun hexToBytes(hex: String): ByteArray {
        val len = hex.length
        val out = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            out[i / 2] = ((Character.digit(hex[i], 16) shl 4) + Character.digit(hex[i + 1], 16)).toByte()
            i += 2
        }
        return out
    }

}