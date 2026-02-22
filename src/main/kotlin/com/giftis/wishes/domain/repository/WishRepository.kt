package com.giftis.wishes.domain.repository

import com.giftis.user.domain.model.User
import com.giftis.wishes.domain.modal.Wish
import java.util.UUID

interface WishRepository {

    fun findAllByUserId(userId: String): List<Wish>

    fun create(
        creator: User,
        title: String,
        note: String?,
        link: String?
    ): Wish

    fun delete(id: UUID)

    fun existsByUserIdAndLink(userId: String, link: String): Boolean

    fun checkOwnership(userId: String, id: UUID): Boolean

}