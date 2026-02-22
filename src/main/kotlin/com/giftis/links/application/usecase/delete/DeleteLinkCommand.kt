package com.giftis.links.application.usecase.delete

import java.util.UUID

data class DeleteLinkCommand(
    val id: UUID,
    val userId: String,
)
