package com.giftis.user.infrastructure.persistence.repositorty

import com.giftis.exceptions.user.UserNotFoundException
import com.giftis.user.domain.model.User
import com.giftis.user.domain.repository.UserRepository
import com.giftis.user.infrastructure.persistence.mapper.UserMapper
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val repository: UserJpaRepository,
    private val mapper: UserMapper
) : UserRepository {

    override fun findById(id: String): User {
        val user = repository.findById(id)
        if (user.isEmpty) throw UserNotFoundException()
        return mapper.toModel(user.get())
    }

    override fun save(user: User): User = mapper.toModel(repository.save(mapper.toEntity(user)))

}