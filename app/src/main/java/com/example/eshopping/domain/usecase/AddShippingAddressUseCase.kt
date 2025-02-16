package com.example.eshopping.domain.usecase

import com.example.eshopping.data.model.ShippingAddress
import com.example.eshopping.domain.repo.Repo
import javax.inject.Inject

class AddShippingAddressUseCase @Inject constructor(private val repo: Repo) {
    suspend fun addShippingAddressUseCase(shippingAddress: ShippingAddress) = repo.addShippingAddress(shippingAddress)
}