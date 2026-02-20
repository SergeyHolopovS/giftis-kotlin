package com.giftis.user.infrastructure.persistence.repositorty

import com.giftis.user.infrastructure.persistence.entity.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserJpaRepository : JpaRepository<UserJpaEntity, String> {

    override fun findById(id: String): Optional<UserJpaEntity>
    
}