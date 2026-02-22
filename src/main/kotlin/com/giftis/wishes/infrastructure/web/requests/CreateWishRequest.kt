package com.giftis.wishes.infrastructure.web.requests

data class CreateWishRequest(
    val title: String,
    val note: String?,
    val link: String?
)
