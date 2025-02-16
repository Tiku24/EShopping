package com.example.eshopping.domain.usecase

import com.example.eshopping.domain.repo.Repo
import javax.inject.Inject

class UpdateShippingAddressUseCase @Inject constructor(private val repo: Repo) {
    suspend fun updateShippingAddressUseCase(addressId:String,updatedFields: Map<String, Any?>) = repo.updateShippingAddress(addressId,updatedFields)
}