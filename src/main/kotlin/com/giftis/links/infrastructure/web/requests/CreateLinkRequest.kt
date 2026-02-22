package com.giftis.links.infrastructure.web.requests

import com.giftis.links.domain.model.LinkType
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateLinkRequest(
    @NotBlank
    val userId: String,
    @NotNull
    @Enumerated(EnumType.STRING)
    val type: LinkType,
)
