package com.giftis.security.infrastructure.persistanse.mapper

import com.giftis.security.domain.model.RefreshToken
import com.giftis.security.infrastructure.persistanse.entity.RefreshTokenJpaEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface RefreshTokenMapper {

    fun toModel(token: RefreshTokenJpaEntity): RefreshToken

    fun toJpaEntity(token: RefreshToken): RefreshTokenJpaEntity

}