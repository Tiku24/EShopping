package com.example.eshopping.domain.usecase

import com.example.eshopping.data.model.Cart
import com.example.eshopping.domain.repo.Repo
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val repo: Repo) {
    suspend fun addToCartUseCase(cartData: Cart) = repo.addToCart(cartData)
}