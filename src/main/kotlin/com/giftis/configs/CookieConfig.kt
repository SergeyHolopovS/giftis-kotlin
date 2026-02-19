package com.giftis.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class CookieConfig (

    @Value($$"${cookies.lifetime}")
    val cookieLifetime: Long,

    @Value($$"${cookies.access-name}")
    val accessTokenName: String,

    @Value($$"${cookies.refresh-name}")
    val refreshTokenName: String,

)