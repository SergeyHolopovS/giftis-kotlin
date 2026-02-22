package com.giftis.wishes.application.usecase.delete

import com.giftis.exceptions.wish.YouAreNotOwnerException
import com.giftis.wishes.domain.repository.WishRepository
import org.springframework.stereotype.Component

@Component
class DeleteWishUseCase(
    private val repository: WishRepository,
) {

    fun execute(command: DeleteWishCommand) {
        // Проверяем владение
        if (
            !repository.checkOwnership(
                command.userId,
                command.id
            )
        ) throw YouAreNotOwnerException()

        // Удаляем хотелку
        repository.delete(
            command.id,
        )
    }

}