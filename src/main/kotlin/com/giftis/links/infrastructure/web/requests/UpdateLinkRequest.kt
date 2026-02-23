package com.giftis.links.infrastructure.web.requests

import com.giftis.links.domain.model.LinkType
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class UpdateLinkRequest(
    @NotNull
    val id: UUID,
    @NotNull
    val newType: LinkType,
)
