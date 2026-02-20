package com.giftis.links.infrastructure.persistence.repository

import com.giftis.links.infrastructure.persistence.entity.LinkJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface LinkJpaRepository : JpaRepository<LinkJpaEntity, UUID> {

    @Query("""
        SELECT l 
        FROM LinkJpaEntity l 
        WHERE l.creator.id = :userId 
           OR l.respondent.id = :userId
    """)
    fun findAllByUserId(@Param("userId") userId: UUID): List<LinkJpaEntity>

    override fun deleteById(id: UUID)

}