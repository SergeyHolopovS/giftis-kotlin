package com.giftis.links.application.usecase.delete

import com.giftis.exceptions.link.YouAreNotOwnerException
import com.giftis.links.domain.repository.LinkRepository
import org.springframework.stereotype.Component

@Component
class DeleteLinkUseCase(
    val repository: LinkRepository
) {

    fun execute(command: DeleteLinkCommand) {
        // Проверяем право владения
        if (
            !repository.checkOwnership(
                command.id,
                command.userId
            )
        ) throw YouAreNotOwnerException()

        // Удаляем связь
        repository.delete(command.id)
    }

}