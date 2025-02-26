package com.example.eshopping.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshopping.common.IMAGES
import com.example.eshopping.common.ResultState
import com.example.eshopping.data.SupabaseClient.supabaseClient
import com.example.eshopping.data.model.Category
import com.example.eshopping.data.model.Cart
import com.example.eshopping.data.model.Product
import com.example.eshopping.data.model.ShippingAddress
import com.example.eshopping.data.model.UserData
import com.example.eshopping.domain.usecase.AddShippingAddressUseCase
import com.example.eshopping.domain.usecase.AddToCartUseCase
import com.example.eshopping.domain.usecase.DeleteCartItemUseCase
import com.example.eshopping.domain.usecase.GetCartItemsUseCase
import com.example.eshopping.domain.usecase.GetCategoryUseCase
import com.example.eshopping.domain.usecase.GetProductUseCase
import com.example.eshopping.domain.usecase.GetShippingAddressUseCase
import com.example.eshopping.domain.usecase.GetSpecificProductUseCase
import com.example.eshopping.domain.usecase.GetUserByIdUseCase
import com.example.eshopping.domain.usecase.RegisterUserWithEmailPassUseCase
import com.example.eshopping.domain.usecase.SearchProductUseCase
import com.example.eshopping.domain.usecase.SignInUserWithEmailPassUseCase
import com.example.eshopping.domain.usecase.UpdateShippingAddressUseCase
import com.example.eshopping.domain.usecase.UpdateUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val registerUserWithEmailPassUseCase: RegisterUserWithEmailPassUseCase,
    private val signInUserWithEmailPassUseCase: SignInUserWithEmailPassUseCase,
    private val getSpecificProductUseCase: GetSpecificProductUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val searchProductUseCase: SearchProductUseCase,
    private val addShippingAddressUseCase: AddShippingAddressUseCase,
    private val getShippingAddressUseCase: GetShippingAddressUseCase,
    private val updateShippingAddressUseCase: UpdateShippingAddressUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase
) :ViewModel() {
    private val _getProductCategoryState = MutableStateFlow(GetProductCategoryState())
    val getProductCategoryState: StateFlow<GetProductCategoryState> = _getProductCategoryState

    private val _registerUserWithEmailPassState = MutableStateFlow(RegisterUserWithEmailPassState())
    val registerUserWithEmailPassState = _registerUserWithEmailPassState.asStateFlow()

    private val _signInUserWithEmailPassState = MutableStateFlow(SignInUserWithEmailPassState())
    val signInUserWithEmailPassState = _signInUserWithEmailPassState.asStateFlow()

    private val _getSpecificProductState = MutableStateFlow(GetSpecificProductState())
    val getSpecificProductState = _getSpecificProductState.asStateFlow()

    private val _isBottomBarVisible = MutableStateFlow(true)
    val isBottomBarVisible = _isBottomBarVisible.asStateFlow()

    private val _getUserByIdState = MutableStateFlow(GetUserByIdState())
    val getUserByIdState = _getUserByIdState.asStateFlow()

    private val _updateUserDataState = MutableStateFlow(UpdateUserDataState())
    val updateUserDataState = _updateUserDataState.asStateFlow()

    private val _searchProductState = MutableStateFlow(SearchProductState())
    val searchProductState = _searchProductState.asStateFlow()

    private val _addShippingAddressState = MutableStateFlow(AddShippingAddressState())
    val addShippingAddressState = _addShippingAddressState.asStateFlow()

    private val _getShippingAddressState = MutableStateFlow(GetShippingAddressState())
    val getShippingAddressState = _getShippingAddressState.asStateFlow()

    private val _updateShippingAddressState = MutableStateFlow(UpdateShippingAddressState())
    val updateShippingAddressState = _updateShippingAddressState.asStateFlow()

    private val _addToCartState = MutableStateFlow(AddToCartState())
    val addToCartState = _addToCartState.asStateFlow()

    private val _getCartItemsState = MutableStateFlow(GetCartItemsState())
    val getCartItemsState = _getCartItemsState.asStateFlow()

    private val _deleteCartItemState = MutableStateFlow(AddToCartState())
    val deleteCartItemState = _deleteCartItemState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val _uploadGetImageState = MutableStateFlow(UploadGetImageState())
    val uploadImageState = _uploadGetImageState.asStateFlow()

    private var allProducts: List<Product> = emptyList()


    var totalAmount = MutableStateFlow(0)


    val _searchQuery = MutableStateFlow("")


    val _selectedSize = MutableStateFlow("")
    val selectedSize: StateFlow<String> get() = _selectedSize

    val _selectedColor = MutableStateFlow("")
    val selectedColor: StateFlow<String> get() = _selectedColor

    fun updateSelectedSize(newSize: String) {
        Log.d("TAGSize", "Before Update: ${_selectedSize.value}")
        _selectedSize.value = newSize
        Log.d("TAGSize", "After Update: ${_selectedSize.value}")
    }

    fun updateSelectedColor(newColor: String) {
        _selectedColor.value = newColor
    }

    fun resetSize(){
        _selectedSize.value = ""
    }

    fun resetColor(){
        _selectedColor.value = ""
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }



    fun searchQuery(){
        viewModelScope.launch {
            _searchQuery
                .debounce(500L)
                .collect{
                    if (it.isNotEmpty()){
                        searchProduct(it)
                    }
                }
        }
    }

    fun selectCategory(category: String){
        _selectedCategory.value = category
        _getProductCategoryState.value = _getProductCategoryState.value.copy(
            productData = _getProductCategoryState.value.productData?.filter { it.category == category }
        )
    }

    fun deleteCartItem(cId: String){
        viewModelScope.launch(Dispatchers.IO) {
            deleteCartItemUseCase.deleteCartItemUseCase(cId).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _deleteCartItemState.value = AddToCartState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _deleteCartItemState.value = AddToCartState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _deleteCartItemState.value = AddToCartState(error = it.message)
                    }
                }
            }
        }

    }

    fun getCartItems(){
        viewModelScope.launch(Dispatchers.IO) {
            getCartItemsUseCase.getCartItemsUseCase().collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _getCartItemsState.value = GetCartItemsState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getCartItemsState.value = GetCartItemsState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _getCartItemsState.value = GetCartItemsState(error = it.message)
                    }
                }
            }
        }
    }

    fun addToCart(cartData: Cart){
        viewModelScope.launch(Dispatchers.IO) {
            addToCartUseCase.addToCartUseCase(cartData).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _addToCartState.value = AddToCartState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _addToCartState.value = AddToCartState(data = it.data)
                        Log.d("TAGCART", "addToCart: ${it.data}")

                    }
                    is ResultState.Error -> {
                        _addToCartState.value = AddToCartState(error = it.message)
                    }
                }
            }
        }
    }

    fun getShippingAddress(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            getShippingAddressUseCase.getShippingAddressUseCase(id).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _getShippingAddressState.value = GetShippingAddressState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getShippingAddressState.value = GetShippingAddressState(success = it.data)
                        Log.d("TAGETSHIP", "getShippingAddress: ${it.data}")
                    }
                    is ResultState.Error -> {
                        _getShippingAddressState.value = GetShippingAddressState(error = it.message)
                    }
                }
            }
        }
    }

    fun addShippingAddress(shippingAddress: ShippingAddress){
        viewModelScope.launch(Dispatchers.IO) {
            addShippingAddressUseCase.addShippingAddressUseCase(shippingAddress).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _addShippingAddressState.value = AddShippingAddressState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _addShippingAddressState.value = AddShippingAddressState(success = it.data)
                    }
                    is ResultState.Error -> {
                        _addShippingAddressState.value = AddShippingAddressState(error = it.message)
                    }
                }
            }
        }
    }

    fun searchProduct(query: String){
        viewModelScope.launch {
            searchProductUseCase.searchProductUseCase(query.uppercase()).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _searchProductState.value = SearchProductState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _searchProductState.value = SearchProductState(success = it.data)
                    }
                    is ResultState.Error -> {
                        _searchProductState.value = SearchProductState(error = it.message)
                    }
                }
            }
        }
    }

    fun updateShippingAddress(addressId:String,updatedFields: Map<String, Any?>){
        viewModelScope.launch(Dispatchers.IO) {
            updateShippingAddressUseCase.updateShippingAddressUseCase(addressId,updatedFields).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _updateShippingAddressState.value = UpdateShippingAddressState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _updateShippingAddressState.value = UpdateShippingAddressState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _updateShippingAddressState.value = UpdateShippingAddressState(error = it.message)
                    }
                }
            }
        }
    }


    fun updateUser(updatedFields: Map<String, Any?>){
        viewModelScope.launch {
            updateUserDataUseCase.updateUserDataUseCase(updatedFields).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _updateUserDataState.value = UpdateUserDataState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _updateUserDataState.value = UpdateUserDataState(success = it.data)
                    }
                    is ResultState.Error -> {
                        _updateUserDataState.value = UpdateUserDataState(error = it.message)
                    }
                }
            }
        }
    }

    fun uploadImage(fileName:String,byteArray: ByteArray){
        viewModelScope.launch {
            try {
                _uploadGetImageState.value = UploadGetImageState(isLoading = true)
                val bucket = supabaseClient.storage[IMAGES]
                bucket.upload("$fileName.png",byteArray,true)
                _uploadGetImageState.value = UploadGetImageState(data = "Image Uploaded Successfully")
            }catch (e:Exception){
                _uploadGetImageState.value = UploadGetImageState(error = e.message.toString())
            }
        }
    }

    fun getImages(bucketName: String,fileName: String, onImageUrlRetrieved: (url:String) -> Unit){
        viewModelScope.launch {
            try {
                _uploadGetImageState.value= UploadGetImageState(isLoading = true)
                val bucket = supabaseClient.storage[bucketName]
                val url = bucket.publicUrl("$fileName.png")
                onImageUrlRetrieved(url)
                _uploadGetImageState.value= UploadGetImageState(data = url)
            }catch (e:Exception){
                _uploadGetImageState.value= UploadGetImageState(error = e.message.toString())
            }
        }
    }


    fun getUserById(uid: String){
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase.getUserByIdUseCase(uid).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _getUserByIdState.value = GetUserByIdState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getUserByIdState.value = GetUserByIdState(success = it.data)
                    }
                    is ResultState.Error -> {
                        _getUserByIdState.value = GetUserByIdState(error = it.message)
                    }
                }
            }
        }
    }

    fun getSpecificProduct(id:String){
        viewModelScope.launch {
            getSpecificProductUseCase.getSpecificProductUseCase(id).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _getSpecificProductState.value = GetSpecificProductState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getSpecificProductState.value = GetSpecificProductState(success = it.data)
                        Log.d("TAGVM", "getSpecificProduct: ${it.data}")
                    }
                    is ResultState.Error -> {
                        _getSpecificProductState.value = GetSpecificProductState(error = it.message)
                    }
                }
            }
        }
    }


    fun registerUserWithEmailPass(userData: UserData){
        viewModelScope.launch(Dispatchers.IO) {
            registerUserWithEmailPassUseCase.registerUserWithEmailPassUserCase(userData).collectLatest{
                when(it){
                    is ResultState.Loading -> {
                        _registerUserWithEmailPassState.value = RegisterUserWithEmailPassState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _registerUserWithEmailPassState.value = RegisterUserWithEmailPassState(success = it.data)
                    }
                    is ResultState.Error -> {
                        _registerUserWithEmailPassState.value = RegisterUserWithEmailPassState(error = it.message)
                    }
                }
            }

        }

    }

    fun signInUserWithEmailPass(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            signInUserWithEmailPassUseCase.signInUserWithEmailPassUseCase(email,password).collectLatest {
                when(it){
                    is ResultState.Loading -> {
                        _signInUserWithEmailPassState.value = SignInUserWithEmailPassState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _signInUserWithEmailPassState.value = SignInUserWithEmailPassState(success = it.data)
                    }
                    is ResultState.Error -> {
                        _signInUserWithEmailPassState.value = SignInUserWithEmailPassState(error = it.message)
                    }
                }
            }
        }
    }


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
    fun resetState(){
        _getProductCategoryState.value = GetProductCategoryState()
        _registerUserWithEmailPassState.value = RegisterUserWithEmailPassState()
        _signInUserWithEmailPassState.value = SignInUserWithEmailPassState()
        _getSpecificProductState.value = GetSpecificProductState()
        _getUserByIdState.value = GetUserByIdState()
        _updateUserDataState.value = UpdateUserDataState()
        _searchProductState.value = SearchProductState()
        _addShippingAddressState.value = AddShippingAddressState()
        _getShippingAddressState.value = GetShippingAddressState()
        _updateShippingAddressState.value = UpdateShippingAddressState()
        _selectedSize.value = ""
        _selectedColor.value = ""
        _searchQuery.value = ""
    }
}


data class GetProductCategoryState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val categoryData: List<Category>? = null,
    val productData: List<Product>? = null
)

data class RegisterUserWithEmailPassState(
    val isLoading: Boolean = false,
    val error: String? = null,
    var success: String? = null
)

data class SignInUserWithEmailPassState(
    val isLoading: Boolean = false,
    val error: String? = null,
    var success: String? = null
)

data class GetSpecificProductState(
    val isLoading:Boolean = false,
    val error: String? = null,
    val success: Product? = null
)

data class GetUserByIdState(
    val isLoading:Boolean = false,
    val error: String? = null,
    val success: UserData? = null
)

data class UpdateUserDataState(
    val isLoading:Boolean = false,
    val error: String? = null,
    var success: String? = null
)

data class SearchProductState(
    val isLoading:Boolean = false,
    val error: String? = null,
    val success: List<Product>? = emptyList()
)

data class AddShippingAddressState(
    val isLoading: Boolean = false,
    val error: String? = null,
    var success: String? = null
)

data class GetShippingAddressState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: List<ShippingAddress>? = emptyList()
)

data class UpdateShippingAddressState(
    val isLoading: Boolean = false,
    val error: String? = null,
    var data: String? = null
)

data class AddToCartState(
    val isLoading: Boolean = false,
    val error: String? = null,
    var data: String? = null
)

data class GetCartItemsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<Cart>? = emptyList()
)

data class UploadGetImageState(
    val isLoading:Boolean = false,
    val error:String = "",
    val data: String = ""
)