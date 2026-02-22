package com.giftis.links.infrastructure.persistence.repository

import com.giftis.links.domain.model.Link
import com.giftis.links.domain.model.LinkType
import com.giftis.links.domain.repository.LinkRepository
import com.giftis.links.infrastructure.mappers.LinkMapper
import com.giftis.links.infrastructure.persistence.entity.LinkJpaEntity
import com.giftis.user.domain.model.User
import com.giftis.user.infrastructure.mappers.UserMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
class LinkRepositoryImpl(
    private val mapper: LinkMapper,
    private val userMapper: UserMapper,
    private val repository: LinkJpaRepository,
) : LinkRepository {

    override fun findById(id: UUID): Link
        = mapper.toModel(
            repository.findById(
                id
            )
            .orElseThrow()
        )

    override fun findAllByUserId(userId: String): List<Link>
        = repository.findAllByUserId(userId).map(mapper::toModel).toList()

    override fun create(
        creator: User,
        respondent: User,
        type: LinkType
    ): Link =
        mapper.toModel(
            repository.save(
                LinkJpaEntity(
                    creator = userMapper.toEntity(creator),
                    respondent = userMapper.toEntity(respondent),
                    type = type
                )
            )
        )

    override fun delete(id: UUID) = repository.deleteById(id)

    override fun existsByUsers(
        user1: User,
        user2: User
    ): Boolean = repository.existsByUsers(user1.id, user2.id)

    override fun ownByUserId(id: UUID, userId: String): Boolean
        = repository.ownByUserId(id, userId)

    @Transactional
    override fun updateType(id: UUID, type: LinkType)
        = repository.updateTypeByLinkId(id, type)

}