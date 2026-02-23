package com.giftis.links.application.usecase.create

import com.giftis.exceptions.link.SuchLinkAlreadyExists
import com.giftis.exceptions.user.CannotCreateLinkYourselfException
import com.giftis.links.domain.repository.LinkRepository
import com.giftis.user.domain.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class CreateLinkUseCase(
    private val linkRepository: LinkRepository,
    private val userRepository: UserRepository
) {

    fun execute(command: CreateLinkCommand) {
        // Предотвращаем связь с самим собой
        if (command.userId == command.respondentId)
            throw CannotCreateLinkYourselfException()

        // Получаем пользователей из бд
        val user = userRepository.findById(command.userId)
        val respondent = userRepository.findById(command.respondentId)

        // Проверяем наличие такой связи
        if (linkRepository.existsByUsers(user, respondent))
            throw SuchLinkAlreadyExists()

        // Создаём новую связь
        linkRepository.create(
            creator = user,
            respondent = respondent,
            type = command.linkType
        )
    }
}