package com.example.eshopping.domain.usecase

import com.example.eshopping.domain.repo.Repo
import javax.inject.Inject

class SignInUserWithEmailPassUseCase @Inject constructor(private val repo: Repo) {
    suspend fun signInUserWithEmailPassUseCase(email:String,password:String) = repo.signInUserWithEmailPass(email,password)
}