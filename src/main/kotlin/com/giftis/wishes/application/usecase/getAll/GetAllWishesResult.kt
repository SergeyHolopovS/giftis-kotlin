package com.giftis.wishes.application.usecase.getAll

import com.giftis.wishes.domain.modal.Wish

data class GetAllWishesResult(
    val list: List<Wish>,
)
