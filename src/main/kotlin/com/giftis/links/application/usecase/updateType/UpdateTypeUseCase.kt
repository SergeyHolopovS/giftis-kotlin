package com.giftis.links.application.usecase.updateType

import com.giftis.links.domain.repository.LinkRepository
import org.springframework.stereotype.Component

@Component
class UpdateTypeUseCase(
    val linkRepository: LinkRepository,
) {

    fun execute(command: UpdateTypeCommand) {
        // Проверяем право владения
        linkRepository.ownByUserId(command.id, command.userId)

        // Изменяем тип
        linkRepository.updateType(command.id, command.newType)
    }

}