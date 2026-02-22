package com.giftis.user.domain.repository

import com.giftis.user.domain.model.User

interface UserRepository {

    fun findById(id: String): User

    fun save(user: User): User

    fun existsById(id: String): Boolean

}