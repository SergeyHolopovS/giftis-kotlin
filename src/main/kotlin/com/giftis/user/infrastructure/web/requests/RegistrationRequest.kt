package com.giftis.user.infrastructure.web.requests

import jakarta.validation.constraints.NotBlank

data class RegistrationRequest(
    @NotBlank
    val initData: String,
)
