package com.giftis.links.infrastructure.web.response

import com.giftis.links.domain.model.LinkType
import com.giftis.user.infrastructure.web.response.UserDto
import java.util.UUID

data class LinkDto(
    val id: UUID,
    val user: UserDto,
    val type: LinkType,
)
