package com.example.eshopping.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eshopping.presentation.viewmodel.MainViewModel
import com.example.eshopping.ui.theme.AppTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreenUI(modifier: Modifier = Modifier,vm: MainViewModel,navController: NavController) {
    val categoryProductState by vm.getProductCategoryState.collectAsState()
    Log.d("Debug", categoryProductState.categoryData.toString())

    LaunchedEffect(Unit) {
        vm.loadProductCategory()
        vm.resetSize()
        vm.resetColor()
    }

    when{
        categoryProductState.error != null -> {
            Log.d("Debug", "Error: ${categoryProductState.error}")
            Toast.makeText(LocalContext.current, categoryProductState.error, Toast.LENGTH_SHORT).show()
        }
        categoryProductState.isLoading -> {
            CircularProgressIndicator(modifier=Modifier.fillMaxSize().wrapContentSize(), color = AppTheme.colorScheme.primary)
        }
        else -> {
            Log.d("Debug", "Data loaded: ${categoryProductState.categoryData}")
            Scaffold(modifier = Modifier.fillMaxSize().padding(6.dp)) {
                ShoppingAppHomeScreen(modifier,categoryProductState, navController = navController,vm)
            }
        }
    }
}


