package com.example.eshopping.presentation.navigation

import kotlinx.serialization.Serializable

sealed class SubNavigation{
    @Serializable
    object MainHomeScreen: SubNavigation()

    @Serializable
    object SignUpSignInScreen: SubNavigation()
}

sealed class Routes {
    @Serializable
    object HomeScreen

    @Serializable
    object SignInScreen

    @Serializable
    object SignUpScreen

    @Serializable
    object ProfileScreen

    @Serializable
    object CartScreen

    @Serializable
    object WishListScreen

    @Serializable
    data class ShippingScreen(
        val name: String,
        val price:String,
        val image:String,
        val quantity:Int
    )

    @Serializable
    data class ProductDetailScreen(val pId:String)

    @Serializable
    object CategoryWiseProductScreen
}
