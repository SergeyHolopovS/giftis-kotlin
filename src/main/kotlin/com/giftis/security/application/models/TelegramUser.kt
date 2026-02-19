package com.giftis.security.application.models

import com.fasterxml.jackson.annotation.JsonProperty

data class TelegramUser(
    val id: Long,
    @JsonProperty("first_name")
    val firstName: String,
    @JsonProperty("last_name")
    val lastName: String,
    val username: String? = null,
    @JsonProperty("language_code")
    val languageCode: String,
    @JsonProperty("allows_write_to_pm")
    val allowsWriteToPm: String? = null,
    @JsonProperty("photo_url")
    val photoUrl: String? = null,
    @JsonProperty("is_premium")
    val isPremium: Boolean? = null,
)
