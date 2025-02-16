package com.example.eshopping.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eshopping.R
import com.example.eshopping.presentation.viewmodel.GetProductCategoryState
import com.example.eshopping.presentation.viewmodel.MainViewModel

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
            Log.d("Debug", "Loading state")
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else -> {
            Log.d("Debug", "Data loaded: ${categoryProductState.categoryData}")
            Scaffold {
                ShoppingAppHomeScreen(modifier,categoryProductState, navController = navController,vm)
            }
        }
    }
}

@Composable
fun CategoriesSection(modifier: Modifier,vm: MainViewModel,categoryProductState: GetProductCategoryState) {

    Column {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Categories", style = MaterialTheme.typography.bodySmall)
            Text(text = "See more", style = MaterialTheme.typography.bodyMedium, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(modifier.padding(10.dp)) {
            items(categoryProductState.categoryData ?: emptyList()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(R.drawable.ic_launcher_background), contentDescription = null)
                    Text(text = it.name, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}


