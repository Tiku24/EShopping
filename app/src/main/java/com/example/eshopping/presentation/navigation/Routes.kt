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
}
