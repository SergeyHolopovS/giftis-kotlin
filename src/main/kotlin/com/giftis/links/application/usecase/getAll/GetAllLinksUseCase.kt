package com.giftis.links.application.usecase.getAll

import com.giftis.links.domain.repository.LinkRepository
import org.springframework.stereotype.Component

@Component
class GetAllLinksUseCase(
    val linkRepository: LinkRepository
) {

    fun execute(command: GetAllLinksCommand): GetAllLinksResult
        = GetAllLinksResult(
            linkRepository.findAllByUserId(
                command.userId
            )
        )

}