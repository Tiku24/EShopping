package com.example.eshopping.domain.usecase

import com.example.eshopping.data.model.UserData
import com.example.eshopping.domain.repo.Repo
import javax.inject.Inject

class RegisterUserWithEmailPassUseCase @Inject constructor(private val repo: Repo) {
    suspend fun registerUserWithEmailPassUserCase(userData: UserData) = repo.registerUserWithEmailPass(userData)

}