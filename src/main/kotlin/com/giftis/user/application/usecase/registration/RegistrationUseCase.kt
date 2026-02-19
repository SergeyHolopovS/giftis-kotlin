package com.giftis.user.application.usecase.registration

import com.giftis.user.domain.model.User
import com.giftis.user.domain.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class RegistrationUseCase(
    private val userRepository: UserRepository
) {

    fun execute(command: RegistrationCommand) {
        val user = User(
            id = command.telegramUser.id.toString(),
            name = command.telegramUser.firstName
        )
        userRepository.save(user)
    }

}