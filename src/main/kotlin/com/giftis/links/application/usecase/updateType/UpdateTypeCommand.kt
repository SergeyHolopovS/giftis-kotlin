package com.giftis.links.application.usecase.updateType

import com.giftis.links.domain.model.LinkType
import java.util.UUID

data class UpdateTypeCommand(
    val id: UUID,
    val userId: String,
    val newType: LinkType,
)
