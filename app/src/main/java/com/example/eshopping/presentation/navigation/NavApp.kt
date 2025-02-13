package com.example.eshopping.presentation.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.eshopping.data.model.Product
import com.example.eshopping.presentation.screen.CartScreenUI
import com.example.eshopping.presentation.screen.HomeScreenUI
import com.example.eshopping.presentation.screen.ProductScreenUI
import com.example.eshopping.presentation.screen.ProfileScreenUI
import com.example.eshopping.presentation.screen.ShippingScreenUI
import com.example.eshopping.presentation.screen.SignInScreenUI
import com.example.eshopping.presentation.screen.SignUpScreenUI
import com.example.eshopping.presentation.screen.WishListScreenUI
import com.example.eshopping.presentation.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavApp(auth: FirebaseAuth) {
    val vm: MainViewModel = hiltViewModel()
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }
    var shouldShowBottomBar by remember { mutableStateOf(false) }
    val currentDestinationAsState = navController.currentBackStackEntryAsState()
    val currentRoute = currentDestinationAsState.value?.destination?.route

    LaunchedEffect(currentRoute) {
        shouldShowBottomBar = when(currentRoute){
            Routes.SignUpScreen::class.qualifiedName, Routes.SignInScreen::class.qualifiedName, Routes.ProductDetailScreen::class.qualifiedName , Routes.ProfileScreen::class.qualifiedName-> false
            else -> true
        }
    }

    val StartScreen = if (auth.currentUser == null){
        SubNavigation.SignUpSignInScreen
    }else{
        SubNavigation.MainHomeScreen
    }

    val items = listOf(
        NavigationItem(
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        NavigationItem(
            label = "WishList",
            selectedIcon = Icons.AutoMirrored.Filled.ExitToApp,
            unselectedIcon = Icons.AutoMirrored.Outlined.ExitToApp
        ),
        NavigationItem(
            label = "Cart",
            selectedIcon = Icons.Filled.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart
        ),
        NavigationItem(
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    items.forEachIndexed { index, navigationItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                                when (selectedIndex) {
                                    0 -> navController.navigate(Routes.HomeScreen)
                                    1 -> navController.navigate(Routes.WishListScreen)
                                    2 -> navController.navigate(Routes.CartScreen)
                                    3 -> navController.navigate(Routes.ProfileScreen)
                                }
                            },
                            icon = {
                                if (selectedIndex == index) {
                                    Icon(
                                        imageVector = navigationItem.selectedIcon,
                                        contentDescription = null
                                    )
                                } else {
                                    Icon(
                                        imageVector = navigationItem.unselectedIcon,
                                        contentDescription = null
                                    )
                                }
                            },
                            label = { Text(navigationItem.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        Box(modifier = Modifier.padding(bottom = if (shouldShowBottomBar) innerPadding.calculateBottomPadding() else 0.dp)){
            NavHost(navController = navController, startDestination = StartScreen) {
                navigation<SubNavigation.SignUpSignInScreen>(startDestination = Routes.SignInScreen){
                    composable<Routes.SignInScreen> {
                        SignInScreenUI(navController = navController, vm = vm)
                    }
                    composable<Routes.SignUpScreen> {
                        SignUpScreenUI(navController=navController,vm=vm)
                    }
                }

                navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen){
                    composable<Routes.HomeScreen> {
                        HomeScreenUI(navController=navController, vm = vm)
                    }
                    composable<Routes.WishListScreen> {
                        WishListScreenUI(vm=vm)
                    }
                    composable<Routes.CartScreen> {
                        CartScreenUI()
                    }
                    composable<Routes.ProfileScreen> {
                        ProfileScreenUI(auth = auth, navController = navController, vm = vm)
                    }
                    composable<Routes.ShippingScreen> {
                        val data = it.toRoute<Product>()
                        ShippingScreenUI(name = data.name, image = data.image, price = data.price, vm = vm)
                    }
                }
                composable<Routes.ProductDetailScreen> {
                    val data = it.toRoute<Product>()
                    ProductScreenUI(id = data.pId, navController = navController, vm = vm)
                }
            }
        }


    }


}

//@Composable
//fun App(modifier: Modifier = Modifier,auth: FirebaseAuth) {
//    val navController = rememberNavController()
//    val StartScreen = if (auth.currentUser == null){
//        SubNavigation.SignUpSignInScreen
//    }else{
//        SubNavigation.MainHomeScreen
//    }
//    NavHost(navController = navController, startDestination = StartScreen) {
//                navigation<SubNavigation.SignUpSignInScreen>(startDestination = Routes.SignInScreen){
//                    composable<Routes.SignInScreen> {
//                        SignInScreenUI(navController = navController)
//                    }
//                    composable<Routes.SignUpScreen> {
//                        SignUpScreenUI(navController=navController)
//                    }
//                }
//
//                navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen){
//                    composable<Routes.HomeScreen> {
//                        MainScreen(navController=navController)
//                    }
//                    composable<Routes.WishListScreen> {
//                        WishListScreenUI()
//                    }
//                    composable<Routes.CartScreen> {
//                        CartScreenUI()
//                    }
//                    composable<Routes.ProfileScreen> {
//                        ProfileScreenUI()
//                    }
//                }
//                composable<Routes.ProductDetailScreen> {
//                    val data = it.toRoute<Product>()
//                    ProductScreenUI(id = data.pId)
//                }
//            }
//}
//
//@SuppressLint("StateFlowValueCalledInComposition")
//@Composable
//fun MainScreen(modifier: Modifier = Modifier,navController: NavController,vm: MainViewModel= hiltViewModel()) {
//
//    var isBottomBarVisible  = vm.isBottomBarVisible.value
//
//    Log.d("Bottom", "MainScreen: $isBottomBarVisible")
//    var selectedIndex by remember { mutableIntStateOf(0) }
//    val items = listOf(
//        NavigationItem(
//            label = "Home",
//            selectedIcon = Icons.Filled.Home,
//            unselectedIcon = Icons.Outlined.Home
//        ),
//        NavigationItem(
//            label = "WishList",
//            selectedIcon = Icons.Filled.ExitToApp,
//            unselectedIcon = Icons.Outlined.ExitToApp
//        ),
//        NavigationItem(
//            label = "Cart",
//            selectedIcon = Icons.Filled.ShoppingCart,
//            unselectedIcon = Icons.Outlined.ShoppingCart
//        ),
//        NavigationItem(
//            label = "Profile",
//            selectedIcon = Icons.Filled.Person,
//            unselectedIcon = Icons.Outlined.Person
//        )
//    )
//
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        bottomBar = {
//            AnimatedVisibility(
//                visible = isBottomBarVisible,
//                enter = slideInVertically(initialOffsetY = { it }),
//                exit = slideOutVertically(targetOffsetY = { it })
//            ) {
//                NavigationBar {
//                    items.forEachIndexed { index, navigationItem ->
//                        NavigationBarItem(
//                            selected = selectedIndex == index,
//                            onClick = {
//                                selectedIndex = index
//                                when (selectedIndex) {
//                                    0 -> navController.navigate(Routes.HomeScreen)
//                                    1 -> {
//                                        navController.navigate(Routes.WishListScreen)
//                                    }
//                                    2 -> navController.navigate(Routes.CartScreen)
//                                    3 -> navController.navigate(Routes.ProfileScreen)
//                                }
//                            },
//                            icon = {
//                                if (selectedIndex == index) {
//                                    Icon(
//                                        imageVector = navigationItem.selectedIcon,
//                                        contentDescription = null
//                                    )
//                                } else {
//                                    Icon(
//                                        imageVector = navigationItem.unselectedIcon,
//                                        contentDescription = null
//                                    )
//                                }
//                            },
//                            label = { Text(navigationItem.label) }
//                        )
//                    }
//                }
//            }
//        }
//    ) { paddingValues ->
//        HomeScreenUI(navController=navController, modifier = modifier.padding(paddingValues))
//    }
//}

data class NavigationItem(val label: String, val selectedIcon: ImageVector,val unselectedIcon: ImageVector)

