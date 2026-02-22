package com.giftis.wishes.infrastructure.persistence.repository

import com.giftis.wishes.infrastructure.persistence.entity.WishJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface WishJpaRepository : JpaRepository<WishJpaEntity, UUID> {

    fun findAllByUserId(userId: String): List<WishJpaEntity>

    override fun deleteById(id: UUID)

    fun existsByUserIdAndLink(userId: String, link: String): Boolean

    fun existsByUserIdAndId(userId: String, id: UUID): Boolean

}