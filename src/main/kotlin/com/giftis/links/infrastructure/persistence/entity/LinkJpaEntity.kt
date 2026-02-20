package com.giftis.links.infrastructure.persistence.entity

import com.giftis.links.domain.model.LinkType
import com.giftis.user.infrastructure.persistence.entity.UserJpaEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.UUID

@Entity
data class LinkJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: UserJpaEntity,

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "respondent_id", nullable = false)
    val respondent: UserJpaEntity,

    @Enumerated(EnumType.STRING)
    val type: LinkType,

)
