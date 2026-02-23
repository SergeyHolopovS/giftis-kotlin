package com.giftis.wishes.infrastructure.web.requests

import jakarta.validation.constraints.NotBlank

data class CreateWishRequest(
    @NotBlank
    val title: String,
    val note: String?,
    val link: String?
)
