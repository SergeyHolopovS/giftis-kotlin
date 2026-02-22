package com.giftis.wishes.infrastructure.web.response

import com.giftis.user.infrastructure.web.response.UserDto
import java.util.UUID

data class WishDto(
    val id: UUID,
    val user: UserDto,
    val title: String,
    val note: String?,
    val link: String?,
)
