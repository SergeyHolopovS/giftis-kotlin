package com.giftis.user.infrastructure.persistence.entity

import com.giftis.links.infrastructure.persistence.entity.LinkJpaEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
data class UserJpaEntity (

    // Telegram Id
    @Id
    val id: String,

    // Имя
    val name: String,

    // То, что хочет пользователь
    // ToDo: Добавить список хотелок

    // Созданные связи
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    val createdLinks: List<LinkJpaEntity> = emptyList(),

    // Принятые связи
    @OneToMany(mappedBy = "respondent", fetch = FetchType.LAZY)
    val respondedLinks: List<LinkJpaEntity> = emptyList(),

) {
    // Объединённый список всех связей
    val links: List<LinkJpaEntity>
        get() = createdLinks + respondedLinks
}