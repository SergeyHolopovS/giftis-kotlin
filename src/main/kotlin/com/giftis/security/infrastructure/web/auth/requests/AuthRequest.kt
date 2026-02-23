package com.giftis.security.infrastructure.web.auth.requests

import jakarta.validation.constraints.NotBlank

data class AuthRequest(
    @NotBlank
    val initData: String,
)