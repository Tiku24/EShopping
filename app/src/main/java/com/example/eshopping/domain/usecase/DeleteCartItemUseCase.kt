package com.example.eshopping.domain.usecase

import com.example.eshopping.domain.repo.Repo
import javax.inject.Inject

class DeleteCartItemUseCase @Inject constructor(private val repo: Repo) {
    suspend fun deleteCartItemUseCase(cId: String) = repo.deleteCartItem(cId)
}