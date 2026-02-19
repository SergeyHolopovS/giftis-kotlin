package com.giftis.user.infrastructure.persistanse.mapper

import com.giftis.user.domain.model.User
import com.giftis.user.infrastructure.persistanse.entity.UserJpaEntity
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {

    fun toModel(user: UserJpaEntity): User

    fun toEntity(user: User): UserJpaEntity

}