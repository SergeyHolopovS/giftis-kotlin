package com.giftis

import com.giftis.links.domain.model.LinkType
import com.giftis.links.infrastructure.persistence.entity.LinkJpaEntity
import com.giftis.links.infrastructure.persistence.repository.LinkJpaRepository
import com.giftis.user.infrastructure.persistence.entity.UserJpaEntity
import com.giftis.user.infrastructure.persistence.repositorty.UserJpaRepository
import com.giftis.wishes.infrastructure.persistence.entity.WishJpaEntity
import com.giftis.wishes.infrastructure.persistence.repository.WishJpaRepository
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class FakeData(
    val userJpaRepository: UserJpaRepository,
    val wishJpaRepository: WishJpaRepository,
    val linkJpaRepository: LinkJpaRepository
) : CommandLineRunner {

    val logger = KotlinLogging.logger {}

    override fun run(vararg args: String) {
        logger.info { "Создание фейковых данных" }
        if (userJpaRepository.findAll().isEmpty()) {
            val users = createUsers()
            createWishes(users)
            createLinks(users)
        } else logger.info("Некоторые данные уже присутствуют в базе данных")
        logger.info { "Создание фейковых данных завершено" }
    }

    private fun createUsers(): List<UserJpaEntity> {
        val users = mutableListOf<UserJpaEntity>()
        for (i in 1..10) {
            users.add(
                UserJpaEntity(
                    id = i.toString(),
                    name = "User $i",
                )
            )
            logger.info("Создан пользователь с id $i")
        }
        return userJpaRepository.saveAll(users)
    }

    private fun createWishes(users: List<UserJpaEntity>): List<WishJpaEntity> {
        val wishes = mutableListOf<WishJpaEntity>()
        for (user in users) {
            for (i in 1..5) {
                wishes.add(
                    WishJpaEntity(
                        user = user,
                        title = "Wish number $i",
                        note = if (i%2 == 0) "Note number ${i/2}" else null,
                        link = if (i%3 == 0) "Link number ${i/3}" else null,
                    )
                )
            }
        }
        val savedWishes = wishJpaRepository.saveAll(wishes)
        savedWishes.forEach { logger.info { "Создана хотелка с id ${it.id}" } }
        return savedWishes
    }

    private fun createLinks(users: List<UserJpaEntity>): List<LinkJpaEntity> {
        val links = mutableListOf<LinkJpaEntity>()
        val user1 = users[0]
        for (user2 in users) {
            if (user1.id == user2.id) continue
            links.add(
                LinkJpaEntity(
                    creator = user1,
                    respondent = user2,
                    type = LinkType.FRIEND,
                )
            )
        }
        val savedLinks = linkJpaRepository.saveAll(links)
        savedLinks.forEach { logger.info { "Создана новая связь с id ${it.id}" } }
        return savedLinks
    }

}