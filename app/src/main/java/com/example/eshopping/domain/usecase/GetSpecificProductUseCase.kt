package com.example.eshopping.domain.usecase

import com.example.eshopping.domain.repo.Repo
import javax.inject.Inject

class GetSpecificProductUseCase @Inject constructor(private val repo: Repo) {
    suspend fun getSpecificProductUseCase(id:String) = repo.getSpecificProduct(id)
}