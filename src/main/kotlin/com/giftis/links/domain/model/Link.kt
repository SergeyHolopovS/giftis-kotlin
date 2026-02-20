package com.giftis.links.domain.model

import com.giftis.user.domain.model.User
import java.util.UUID

data class Link(
    val id: UUID,
    val creator: User,
    val respondent: User,
    val type: LinkType,
)
