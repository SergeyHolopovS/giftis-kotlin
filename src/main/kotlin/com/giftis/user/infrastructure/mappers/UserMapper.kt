package com.giftis.user.infrastructure.mappers

import com.giftis.user.domain.model.User
import com.giftis.user.infrastructure.persistence.entity.UserJpaEntity
import com.giftis.user.infrastructure.web.response.UserDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {

    fun toModel(user: UserJpaEntity): User

    @Mapping(target = "createdLinks", expression = "java(java.util.Collections.emptyList())") // Устанавливаем пустой список для createdLinks
    @Mapping(target = "respondedLinks", expression = "java(java.util.Collections.emptyList())") // Устанавливаем пустой список для respondedLinks
    fun toEntity(user: User): UserJpaEntity

    fun toDto(user: User): UserDto

}