package com.giftis.wishes.application.usecase.getAll

import com.giftis.wishes.domain.repository.WishRepository
import org.springframework.stereotype.Component

@Component
class GetAllWishesUseCase(
    val repository: WishRepository
) {

    fun execute(command: GetAllWishesCommand): GetAllWishesResult
        = GetAllWishesResult(
            repository.findAllByUserId(
                command.userId
            )
        )

}