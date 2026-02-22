package com.giftis.user.application.usecase.registration

import com.giftis.exceptions.user.UserAlreadyExistsException
import com.giftis.user.domain.model.User
import com.giftis.user.domain.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class RegistrationUseCase(
    private val userRepository: UserRepository
) {

    fun execute(command: RegistrationCommand) {
        // Проверяем существование такого пользователя
        if (
            userRepository.existsById(
                command
                    .telegramUser
                    .id
                    .toString()
            )
        ) throw UserAlreadyExistsException()

        // Создаём нового пользователя
        val user = User(
            id = command.telegramUser.id.toString(),
            name = command.telegramUser.firstName
        )

        // Сохраняем его в бд
        userRepository.save(user)
    }

}