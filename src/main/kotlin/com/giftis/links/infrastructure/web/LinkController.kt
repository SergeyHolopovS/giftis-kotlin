package com.giftis.links.infrastructure.web

import com.giftis.links.application.usecase.create.CreateLinkCommand
import com.giftis.links.application.usecase.create.CreateLinkUseCase
import com.giftis.links.application.usecase.delete.DeleteLinkCommand
import com.giftis.links.application.usecase.delete.DeleteLinkUseCase
import com.giftis.links.application.usecase.getAll.GetAllLinksCommand
import com.giftis.links.application.usecase.getAll.GetAllLinksUseCase
import com.giftis.links.application.usecase.updateType.UpdateTypeCommand
import com.giftis.links.application.usecase.updateType.UpdateTypeUseCase
import com.giftis.links.infrastructure.mappers.LinkMapper
import com.giftis.links.infrastructure.web.requests.CreateLinkRequest
import com.giftis.links.infrastructure.web.requests.UpdateLinkRequest
import com.giftis.links.infrastructure.web.response.LinkDto
import com.giftis.user.infrastructure.mappers.UserMapper
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Validated
@RestController
@RequestMapping("/v1/links")
class LinkController(
    private val createLinkUseCase: CreateLinkUseCase,
    private val updateTypeUseCase: UpdateTypeUseCase,
    private val getAllLinksUseCase: GetAllLinksUseCase,
    private val deleteLinkUseCase: DeleteLinkUseCase,
    private val mapper: LinkMapper,
    private val userMapper: UserMapper,
) {

    @GetMapping
    fun getLinks(
        @AuthenticationPrincipal userId: String,
    ): ResponseEntity<List<LinkDto>> {
        val result = getAllLinksUseCase.execute(
            GetAllLinksCommand(
                userId = userId,
            )
        )
        return ResponseEntity.ok(
            result
                .list
                .map { mapper.toDto(it, userId, userMapper) }
        )
    }

    @PostMapping
    fun createLink(
        @AuthenticationPrincipal userId: String,
        @Valid @RequestBody request: CreateLinkRequest
    ): ResponseEntity<Unit> {
        createLinkUseCase.execute(
            CreateLinkCommand(
                userId = userId,
                respondentId = request.userId,
                linkType = request.type
            )
        )
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PatchMapping("/type")
    fun updateLinkType(
        @AuthenticationPrincipal userId: String,
        @Valid @RequestBody request: UpdateLinkRequest,
    ): ResponseEntity<Unit> {
        updateTypeUseCase.execute(
            UpdateTypeCommand(
                id = request.id,
                userId = userId,
                newType = request.newType,
            )
        )
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun deleteLink(
        @AuthenticationPrincipal userId: String,
        @PathVariable id: UUID
    ): ResponseEntity<Unit> {
        deleteLinkUseCase.execute(
            DeleteLinkCommand(
                id = id,
                userId = userId,
            )
        )
        return ResponseEntity.ok().build()
    }

}