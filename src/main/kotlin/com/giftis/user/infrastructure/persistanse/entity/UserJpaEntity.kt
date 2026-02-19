package com.giftis.user.infrastructure.persistanse.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class UserJpaEntity (

    // Telegram Id
    @Id
    val id: String,

    // Имя
    val name: String,

    // То, что хочет пользователь
    // ToDo: Добавить список хотелок

    // Связи с другими пользователями
    // Todo: Добавить список связей

)