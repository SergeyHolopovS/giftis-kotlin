package com.giftis.wishes.infrastructure.web

import com.giftis.wishes.application.usecase.create.CreateWishCommand
import com.giftis.wishes.application.usecase.create.CreateWishUseCase
import com.giftis.wishes.application.usecase.delete.DeleteWishCommand
import com.giftis.wishes.application.usecase.delete.DeleteWishUseCase
import com.giftis.wishes.application.usecase.getAll.GetAllWishesCommand
import com.giftis.wishes.application.usecase.getAll.GetAllWishesUseCase
import com.giftis.wishes.domain.modal.Wish
import com.giftis.wishes.infrastructure.mappers.WishMapper
import com.giftis.wishes.infrastructure.web.requests.CreateWishRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/wish")
class WishController(
    private val createWishUseCase: CreateWishUseCase,
    private val deleteWishUseCase: DeleteWishUseCase,
    private val getAllWishesUseCase: GetAllWishesUseCase,
    private val mapper: WishMapper,
) {

    @GetMapping("/user/{userId}")
    fun getAllWishes(
        @PathVariable userId: String,
    ) = ResponseEntity.ok(
        getAllWishesUseCase.execute(
            GetAllWishesCommand(
                userId = userId,
            )
        )
            .list
            .map { mapper.toDto(it) }
    )

    @PostMapping
    fun createWish(
        @AuthenticationPrincipal userId: String,
        @RequestBody request: CreateWishRequest
    ): ResponseEntity<Wish> {
        createWishUseCase.execute(
            CreateWishCommand(
                userId = userId,
                title = request.title,
                note = request.note,
                link = request.link,
            )
        )
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/{id}")
    fun deleteWish(
        @AuthenticationPrincipal userId: String,
        @PathVariable id: UUID
    ): ResponseEntity<Unit> {
        deleteWishUseCase.execute(
            DeleteWishCommand(
                userId = userId,
                id = id
            )
        )
        return ResponseEntity.ok().build()
    }

}