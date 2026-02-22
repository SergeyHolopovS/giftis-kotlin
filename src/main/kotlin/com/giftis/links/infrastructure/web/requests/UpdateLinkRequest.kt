package com.giftis.links.infrastructure.web.requests

import com.giftis.links.domain.model.LinkType
import java.util.UUID

data class UpdateLinkRequest(
    val id: UUID,
    val newType: LinkType,
)
