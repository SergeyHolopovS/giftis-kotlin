package com.giftis.links.domain.repository

import com.giftis.links.domain.model.Link
import java.util.UUID

interface LinkRepository {

    fun findByUserId(userId: String): List<Link>

    fun save(link: Link): Link

    fun delete(id: UUID)

}