package com.giftis.wishes.infrastructure.persistence.repository

import com.giftis.wishes.infrastructure.persistence.entity.WishJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface WishJpaRepository : JpaRepository<WishJpaEntity, UUID> {

    fun findAllByUser_Id(userId: String): List<WishJpaEntity>

    override fun deleteById(id: UUID)

    fun existsByUser_IdAndLink(userId: String, link: String): Boolean

    @Query(
        """
            SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
            FROM WishJpaEntity e 
            WHERE e.user.id=:userId AND e.id = :id
        """
    )
    fun checkOwnership(
        @Param("userId") userId: String,
        @Param("id") id: UUID
    ): Boolean

}