package com.giftis.links.application.usecase.getAll

import com.giftis.links.domain.model.Link

data class GetAllLinksResult(
    val list: List<Link>
)
