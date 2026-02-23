package com.giftis.links.infrastructure.mappers

import com.giftis.links.domain.model.Link
import com.giftis.links.infrastructure.persistence.entity.LinkJpaEntity
import com.giftis.links.infrastructure.web.response.LinkDto
import com.giftis.user.infrastructure.mappers.UserMapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", uses = [UserMapper::class], unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface LinkMapper {

    fun toModel(linkJpaEntity: LinkJpaEntity): Link

    fun toDto(link: Link, currentUserId: String, userMapper: UserMapper): LinkDto {
        val otherUser = if (link.creator.id != currentUserId) link.creator else link.respondent
        return LinkDto(
            id = link.id,
            user = userMapper.toDto(otherUser),
            type = link.type
        )
    }

}