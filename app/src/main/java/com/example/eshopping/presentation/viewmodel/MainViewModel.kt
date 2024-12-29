package com.example.eshopping.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshopping.common.ResultState
import com.example.eshopping.data.model.Category
import com.example.eshopping.data.model.Product
import com.example.eshopping.domain.usecase.GetCategoryUseCase
import com.example.eshopping.domain.usecase.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getProductUseCase: GetProductUseCase
) :ViewModel() {
    private val _getProductCategoryState = MutableStateFlow(GetProductCategoryState())
    val getProductCategoryState = _getProductCategoryState.asStateFlow()


     fun loadProductCategory(){
        viewModelScope.launch {
            combine(getCategoryUseCase.getCategoryUseCase(),getProductUseCase.getProductUseCase()){ category,product->
                when {
                    category is ResultState.Error -> GetProductCategoryState(
                        isLoading = false,
                        error = category.message
                    )
                    product is ResultState.Error -> GetProductCategoryState(
                        isLoading = false,
                        error = product.message
                    )
                    category is ResultState.Success && product is ResultState.Success -> GetProductCategoryState(
                        isLoading = false,
                        categoryData = category.data,
                        productData = product.data
                    )
                    else -> GetProductCategoryState(isLoading = true)
                }
            }.catch { e ->
                _getProductCategoryState.value = GetProductCategoryState(
                    isLoading = false,
                    error = e.message
                )
            }.collect{
                _getProductCategoryState.value = it
                Log.d("TAGViewModel", it.toString())
            }
        }
    }
}


data class GetProductCategoryState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val categoryData: List<Category>? = null,
    val productData: List<Product>? = null
)