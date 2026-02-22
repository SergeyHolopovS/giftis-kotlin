package com.giftis.wishes.domain.modal

import com.giftis.user.domain.model.User
import java.util.UUID

data class Wish(
    val id: UUID,
    val user: User,
    val title: String,
    val note: String?,
    val link: String?,
)
