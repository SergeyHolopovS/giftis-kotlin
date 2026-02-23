package com.giftis.security.infrastructure.mappers

import com.fasterxml.jackson.core.JsonProcessingException
import com.giftis.exceptions.auth.TokenUnauthorizedException
import com.giftis.security.application.models.TelegramUser
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
    private val maxAuthAge: Long,
    @Value($$"${spring.profiles.active}")
    private val profile: String
) {

    private val hmacAlgorithm = "HmacSHA256"
    private val webAppData = "WebAppData"

    @OptIn(ExperimentalTime::class)
    fun parseInitData(initData: String): TelegramUser {
        if (profile == "dev") return TelegramUser(
            id = initData.toLong(),
            firstName = "User $initData",
            lastName = "User $initData lastName",
            isPremium = false,
            photoUrl = "",
            username = "",
            languageCode = "ru",
            allowsWriteToPm = "true"
        )

        val data = parseQueryString(initData).toMutableMap()

        // Удаляем hash из мапа, записывая его в переменную
        val receivedHash = data.remove("hash") ?: throw TokenUnauthorizedException()

        // Получаем User, чтобы запарсить если пройдём валидацию
        val userEncoded = URLDecoder.decode(
            data["user"] ?: throw TokenUnauthorizedException(),
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
        val secretKey = generateSecretKey()

        // Шифруем dataCheckString по secretKey
        val calculatedHash = hmacSha256(
            dataCheckString.toByteArray(StandardCharsets.UTF_8),
            secretKey
        )

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

    private fun generateSecretKey(): ByteArray {
        val secretKeySpec = SecretKeySpec(webAppData.toByteArray(StandardCharsets.UTF_8), hmacAlgorithm)
        val mac = Mac.getInstance(hmacAlgorithm)
        mac.init(secretKeySpec)
        return mac.doFinal(token.toByteArray(StandardCharsets.UTF_8))
    }

    private fun hmacSha256(data: ByteArray, key: ByteArray): ByteArray {
        val mac = Mac.getInstance(hmacAlgorithm)
        val secretKeySpec = SecretKeySpec(key, hmacAlgorithm)
        mac.init(secretKeySpec)
        return mac.doFinal(data)
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