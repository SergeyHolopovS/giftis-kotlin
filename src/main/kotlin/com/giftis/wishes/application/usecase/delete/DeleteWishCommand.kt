package com.giftis.wishes.application.usecase.delete

import java.util.UUID

data class DeleteWishCommand(
    val userId: String,
    val id: UUID
)
