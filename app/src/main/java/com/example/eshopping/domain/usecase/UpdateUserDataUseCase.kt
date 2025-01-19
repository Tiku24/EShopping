package com.example.eshopping.domain.usecase

import com.example.eshopping.data.model.UserData
import com.example.eshopping.domain.repo.Repo
import javax.inject.Inject

class UpdateUserDataUseCase @Inject constructor(private val repo: Repo) {
    suspend fun updateUserDataUseCase(updatedFields: Map<String, Any?>) = repo.updateUserData(updatedFields)
}