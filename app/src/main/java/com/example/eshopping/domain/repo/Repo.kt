package com.example.eshopping.domain.repo

import com.example.eshopping.common.ResultState
import com.example.eshopping.data.model.Category
import com.example.eshopping.data.model.Product
import kotlinx.coroutines.flow.Flow

interface Repo {
    suspend fun getAllCategory(): Flow<ResultState<List<Category>>>
    suspend fun getAllProduct(): Flow<ResultState<List<Product>>>

}