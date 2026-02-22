package com.giftis.wishes.infrastructure.persistence.repository

import com.giftis.user.domain.model.User
import com.giftis.user.infrastructure.mappers.UserMapper
import com.giftis.wishes.domain.modal.Wish
import com.giftis.wishes.domain.repository.WishRepository
import com.giftis.wishes.infrastructure.mappers.WishMapper
import com.giftis.wishes.infrastructure.persistence.entity.WishJpaEntity
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class WishrepositoryImpl(
    private val repository: WishJpaRepository,
    private val userMapper: UserMapper,
    private val mapper: WishMapper
) : WishRepository {

    override fun findAllByUserId(userId: String): List<Wish>
        = repository.findAllByUserId(userId).map { mapper.toModel(it) }

    override fun create(
        creator: User,
        title: String,
        note: String?,
        link: String?
    ): Wish
        = mapper.toModel(
            repository.save(
                WishJpaEntity(
                    user = userMapper.toEntity(creator),
                    title = title,
                    note = note,
                    link = link
                )
            )
        )

    override fun delete(id: UUID)
        = repository.deleteById(id)

    override fun existsByUserIdAndLink(userId: String, link: String): Boolean
        = repository.existsByUserIdAndLink(userId, link)

    override fun checkOwnership(userId: String, id: UUID): Boolean
        = repository.existsByUserIdAndId(userId, id)

}