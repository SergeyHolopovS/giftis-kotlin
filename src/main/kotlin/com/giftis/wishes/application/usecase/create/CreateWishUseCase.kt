package com.giftis.wishes.application.usecase.create

import com.giftis.exceptions.wish.SuchUrlAlreadyExists
import com.giftis.user.domain.repository.UserRepository
import com.giftis.wishes.domain.repository.WishRepository
import org.springframework.stereotype.Component

@Component
class CreateWishUseCase(
    private val repository: WishRepository,
    private val userRepository: UserRepository,
) {

    fun execute(command: CreateWishCommand) {
        // Получаем пользователя
        val user = userRepository.findById(command.userId)

        // Проверяем есть ли хотелка с таким link
        if (
            command.link != null &&
            repository.existsByUserIdAndLink(
                userId = command.userId,
                link = command
                    .link
                    .replace("http://", "")
                    .replace("https://", "")
                    .replace(Regex("/+$"), "")
            )
        ) throw SuchUrlAlreadyExists()

        // Создаём новую хотелку
        repository.create(
            creator = user,
            title = command.title,
            note = command.note,
            link = command.link
        )
    }

}