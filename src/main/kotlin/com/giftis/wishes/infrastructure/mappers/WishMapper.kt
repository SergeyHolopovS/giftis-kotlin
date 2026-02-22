package com.giftis.wishes.infrastructure.mappers

import com.giftis.wishes.domain.modal.Wish
import com.giftis.wishes.infrastructure.persistence.entity.WishJpaEntity
import com.giftis.wishes.infrastructure.web.response.WishDto
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface WishMapper {

    fun toModel(entity: WishJpaEntity): Wish

    fun toDto(model: Wish): WishDto

}