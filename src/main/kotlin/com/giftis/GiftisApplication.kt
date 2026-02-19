package com.giftis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GiftisApplication

fun main(args: Array<String>) {
	runApplication<GiftisApplication>(*args)
}
