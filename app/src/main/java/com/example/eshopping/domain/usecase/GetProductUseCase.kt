package com.example.eshopping.domain.usecase

import com.example.eshopping.domain.repo.Repo
import javax.inject.Inject

class GetProductUseCase @Inject constructor(private val repo: Repo) {
   suspend fun getProductUseCase() = repo.getAllProduct()
}