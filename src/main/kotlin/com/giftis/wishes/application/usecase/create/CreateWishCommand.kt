package com.giftis.wishes.application.usecase.create

data class CreateWishCommand(
    val userId: String,
    val title: String,
    val note: String?,
    val link: String?,
)
