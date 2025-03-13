package com.example.eshopping.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.eshopping.data.model.Category
import com.example.eshopping.presentation.navigation.Routes
import com.example.eshopping.presentation.viewmodel.GetProductCategoryState
import com.example.eshopping.presentation.viewmodel.MainViewModel
import com.example.eshopping.presentation.viewmodel.SearchProductState
import com.example.eshopping.ui.theme.AppTheme


@Composable
fun ShoppingAppHomeScreen(modifier: Modifier, categoryProductState: GetProductCategoryState,navController: NavController,vm: MainViewModel) {
    Column(
        modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.onPrimary)
            .padding(horizontal = 5.dp)
    ) {
        val searchState = vm.searchProductState.collectAsStateWithLifecycle()
        val query = remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            vm.searchQuery()
        }
        SearchBarNotification(query,searchState,vm)
        if (query.value.isNotEmpty() && searchState.value.success!!.isNotEmpty()){
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(45.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(45.dp)
            ) {
                items(searchState.value.success ?: emptyList()){
                    ProductItem(imageUrl = it.image, name = it.name, price = it.price, finalPrice = it.finalPrice, onClick = {
                        navController.navigate(Routes.ProductDetailScreen(it.pId))
                    },vm)
                }
            }
        }
        else {
            CategorySection(categoryProductState,vm,navController)
            ProductGrid(categoryProductState, navController,vm)
        }
    }
}

@Composable
fun SearchBarNotification(query: MutableState<String>, searchState: State<SearchProductState>, vm: MainViewModel) {


    when{
        searchState.value.isLoading -> {
            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(),color = AppTheme.colorScheme.primary)
        }
        searchState.value.success != null -> {
            Log.d("SearchBar", searchState.value.success.toString())
        }
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = query.value,
            onValueChange = {
                query.value = it
                vm.onSearchQueryChange(it)
            },
            leadingIcon = {Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")},
            colors = TextFieldDefaults.colors(focusedIndicatorColor = AppTheme.colorScheme.primary, unfocusedContainerColor = AppTheme.colorScheme.onPrimary, focusedContainerColor = AppTheme.colorScheme.onPrimary),
            placeholder = { Text("Search", style = AppTheme.typography.labelLarge) },
            shape = AppTheme.shape.container,
            modifier = Modifier
                .weight(1f)
                .height(53.dp)
                .border(
                    width = 1.dp,
                    color = AppTheme.colorScheme.primary,
                    shape = AppTheme.shape.container
                )
        )
        Icon(imageVector = Icons.Outlined.Notifications, tint = AppTheme.colorScheme.primary,contentDescription = "notification icon",modifier = Modifier.size(AppTheme.sizes.larger))
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun CategorySection(categoryProductState: GetProductCategoryState,vm: MainViewModel,navController: NavController) {

    Column {
        Text(
            text = "Categories",
            color = AppTheme.colorScheme.primary,
            style = AppTheme.typography.flashTitle
        )
        LazyRow (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categoryProductState.categoryData ?: emptyList()){
                CategoryChip(it, onClick = {vm.selectCategory(it.name)
                navController.navigate(Routes.CategoryWiseProductScreen)
                })
            }
        }
    }
}

@Composable
fun CategoryChip(category: Category,onClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal =12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .size(70.dp)
            .border(width = 1.dp, color = AppTheme.colorScheme.primary, shape = CircleShape)
            .clickable {
                onClick()
            },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(model = category.imageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(45.dp))
        }
        Spacer(Modifier.height(5.dp))
        Text(category.name, style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.primary)
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProductGrid(categoryProductState: GetProductCategoryState,navController: NavController,vm:MainViewModel) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Flash Sale", color = AppTheme.colorScheme.primary,
            style = AppTheme.typography.flashTitle)

        Text("See more",
            style = AppTheme.typography.labelLarge,
            color = AppTheme.colorScheme.priceColor,
            modifier = Modifier.clickable {
                navController.navigate(Routes.CategoryWiseProductScreen)
            })
    }
    LazyRow {
        items(categoryProductState.productData ?: emptyList()){
            ProductItem(vm = vm, name = it.name, price = it.price, finalPrice = it.finalPrice, imageUrl = it.image, onClick = {
            navController.navigate(Routes.ProductDetailScreen(it.pId))
                })
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProductItem(imageUrl:String,name:String,price:String,finalPrice:String,onClick:()->Unit,vm: MainViewModel) {
    var isBottomBarVisible  = vm.isBottomBarVisible.value
    Column {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(145.dp)
                .height(190.dp)
                .padding(end = 8.dp)
                .clip(AppTheme.shape.container)

        )
        Card(
            onClick = {
                onClick.invoke()},
            modifier = Modifier
                .size(width = 143.dp, height = 160.dp)
                .padding(end = 8.dp, top = 8.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(name, style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.Bottom) {
                    Text("Rs: ", style = AppTheme.typography.labelLarge, color = AppTheme.colorScheme.priceColor)
                    Text(finalPrice, style = AppTheme.typography.labelLarge, color = AppTheme.colorScheme.priceColor)
                }
                Row(modifier = Modifier, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text("Rs: ", style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.primary)
                    Text(price, style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.primary, textDecoration = TextDecoration.LineThrough)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("20% off",style = AppTheme.typography.labelSmall, color = AppTheme.colorScheme.priceColor)
                }
            }
        }
    }
}

