package com.giftis.links.domain.repository

import com.giftis.links.domain.model.Link
import com.giftis.links.domain.model.LinkType
import com.giftis.user.domain.model.User
import java.util.UUID

interface LinkRepository {

    fun findById(id: UUID): Link

    fun findAllByUserId(userId: String): List<Link>

    fun create(
        creator: User,
        respondent: User,
        type: LinkType
    ): Link

    fun delete(id: UUID)

    fun existsByUsers(user1: User, user2: User): Boolean

    fun checkOwnership(id: UUID, userId: String): Boolean

    fun updateType(id: UUID, type: LinkType)

}