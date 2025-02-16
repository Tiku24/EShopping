package com.example.eshopping.domain.usecase

import com.example.eshopping.domain.repo.Repo
import javax.inject.Inject

class GetShippingAddressUseCase @Inject constructor(private val repo: Repo) {
    suspend fun getShippingAddressUseCase(id:String) = repo.getShippingAddress(id)
}