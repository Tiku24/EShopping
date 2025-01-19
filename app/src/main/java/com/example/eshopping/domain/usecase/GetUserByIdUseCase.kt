package com.example.eshopping.domain.usecase

import com.example.eshopping.domain.repo.Repo
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(private val repo: Repo) {
    suspend fun getUserByIdUseCase(uid: String) = repo.getUserById(uid)
}