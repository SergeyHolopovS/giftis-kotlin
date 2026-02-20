package com.giftis.security.infrastructure.mappers

import com.giftis.security.domain.model.RefreshToken
import com.giftis.security.infrastructure.persistence.entity.RefreshTokenJpaEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface RefreshTokenMapper {

    fun toModel(token: RefreshTokenJpaEntity): RefreshToken

    fun toJpaEntity(token: RefreshToken): RefreshTokenJpaEntity

}