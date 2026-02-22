package com.giftis.links.application.usecase.create

import com.giftis.links.domain.model.LinkType

data class CreateLinkCommand (
    val userId: String,
    val respondentId: String,
    var linkType: LinkType,
)