package com.giftis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class GiftisApplication

fun main(args: Array<String>) {
	runApplication<GiftisApplication>(*args)
}
