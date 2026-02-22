package com.giftis.links.infrastructure.web.response

import com.giftis.links.domain.model.LinkType
import com.giftis.user.infrastructure.web.response.UserDto
import java.util.UUID

data class LinkDto(
    val id: UUID,
    val creator: UserDto,
    val respondent: UserDto,
    val type: LinkType,
)
