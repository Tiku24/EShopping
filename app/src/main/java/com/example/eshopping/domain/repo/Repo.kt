package com.example.eshopping.domain.repo

import com.example.eshopping.common.ResultState
import com.example.eshopping.data.model.Category
import com.example.eshopping.data.model.Product
import com.example.eshopping.data.model.UserData
import kotlinx.coroutines.flow.Flow

interface Repo {
    suspend fun getAllCategory(): Flow<ResultState<List<Category>>>
    suspend fun getAllProduct(): Flow<ResultState<List<Product>>>
    suspend fun registerUserWithEmailPass(userData: UserData): Flow<ResultState<String>>
    suspend fun signInUserWithEmailPass(email: String, password: String): Flow<ResultState<String>>
    suspend fun getSpecificProduct(id:String): Flow<ResultState<Product>>
    suspend fun getUserById(uid: String): Flow<ResultState<UserData>>
    suspend fun updateUserData(updatedFields: Map<String, Any?>): Flow<ResultState<String>>
    suspend fun searchProduct(query:String) : Flow<ResultState<List<Product>>>
}