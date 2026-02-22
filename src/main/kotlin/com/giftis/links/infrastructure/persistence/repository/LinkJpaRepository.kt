package com.giftis.links.infrastructure.persistence.repository

import com.giftis.links.domain.model.LinkType
import com.giftis.links.infrastructure.persistence.entity.LinkJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface LinkJpaRepository : JpaRepository<LinkJpaEntity, UUID> {

    @Query("""
        SELECT l 
        FROM LinkJpaEntity l 
        WHERE l.creator.id = :userId 
           OR l.respondent.id = :userId
    """)
    fun findAllByUserId(@Param("userId") userId: String): List<LinkJpaEntity>

    @Query("""
        SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END
        FROM LinkJpaEntity l
        WHERE (l.creator.id = :user1 AND l.respondent.id = :user2)
           OR (l.creator.id = :user2 AND l.respondent.id = :user1)
    """)
    fun existsByUsers(@Param("user1") user1: String,
                      @Param("user2") user2: String): Boolean

    @Query("""
        SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END
        FROM LinkJpaEntity l
        WHERE l.id = :id AND 
        (l.creator.id = :userId OR l.respondent.id = :userId)
    """)
    fun ownByUserId(@Param("id") id: UUID, @Param("user") user: String): Boolean

    @Modifying
    @Transactional
    @Query("""
        UPDATE LinkJpaEntity l
        SET l.type = :newType
        WHERE l.id = :linkId
    """)
    fun updateTypeByLinkId(
        @Param("linkId") linkId: UUID,
        @Param("newType") newType: LinkType
    )

    override fun deleteById(id: UUID)

}