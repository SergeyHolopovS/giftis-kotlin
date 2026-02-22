package com.giftis.links.infrastructure.mappers

import com.giftis.links.domain.model.Link
import com.giftis.links.infrastructure.persistence.entity.LinkJpaEntity
import com.giftis.links.infrastructure.web.response.LinkDto
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface LinkMapper {

    fun toModel(linkJpaEntity: LinkJpaEntity): Link

    fun toDto(model: Link): LinkDto

}