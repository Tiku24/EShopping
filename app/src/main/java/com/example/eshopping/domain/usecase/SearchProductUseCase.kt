package com.example.eshopping.domain.usecase

import com.example.eshopping.domain.repo.Repo
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(private val repo: Repo) {
    suspend fun searchProductUseCase(query: String) = repo.searchProduct(query)
}