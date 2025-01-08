package com.example.eshopping.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.eshopping.presentation.screen.HomeScreenUI
import com.example.eshopping.presentation.screen.SignInScreenUI
import com.example.eshopping.presentation.screen.SignUpScreenUI
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavApp(modifier: Modifier = Modifier,auth: FirebaseAuth) {
    val navController = rememberNavController()

    val StartScreen = if (auth.currentUser == null){
        SubNavigation.SignUpSignInScreen
    }else{
        SubNavigation.MainHomeScreen
    }

    NavHost(navController = navController, startDestination = StartScreen) {
        navigation<SubNavigation.SignUpSignInScreen>(startDestination = Routes.SignInScreen){
            composable<Routes.SignInScreen> {
                SignInScreenUI(modifier, navController = navController)
            }
            composable<Routes.SignUpScreen> {
                SignUpScreenUI(modifier,navController=navController)
            }
        }

        navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen){
            composable<Routes.HomeScreen> {
                HomeScreenUI(modifier)
            }
            composable<Routes.ProfileScreen> {

            }
        }
    }

}