package com.example.eshopping.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.eshopping.data.model.Category
import com.example.eshopping.presentation.navigation.Routes
import com.example.eshopping.presentation.viewmodel.GetProductCategoryState
import com.example.eshopping.presentation.viewmodel.MainViewModel
import com.example.eshopping.presentation.viewmodel.SearchProductState
import com.example.eshopping.ui.theme.Montserrat
import com.example.eshopping.ui.theme.MontserratMedium
import com.example.eshopping.ui.theme.MontserratRegular


@Composable
fun ShoppingAppHomeScreen(modifier: Modifier, categoryProductState: GetProductCategoryState,navController: NavController,vm: MainViewModel) {
    Column(
        modifier
            .fillMaxSize()
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
                .wrapContentSize())
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
            colors = TextFieldDefaults.colors(focusedIndicatorColor = Color(232, 144, 142), unfocusedContainerColor = Color.White, focusedContainerColor = Color.White),
            placeholder = { Text("Search") },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .padding(horizontal = 5.dp)
                .border(
                    width = 1.dp,
                    color = Color(232, 144, 142),
                    shape = RoundedCornerShape(15.dp)
                )
        )
        Icon(imageVector = Icons.Outlined.Notifications, tint = Color.Black,contentDescription = "notification icon",modifier = Modifier.size(32.dp))
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun CategorySection(categoryProductState: GetProductCategoryState,vm: MainViewModel,navController: NavController) {
    val context = LocalContext.current
    Column {
        Text(
            text = "Categories",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratMedium
            )
        )
        LazyRow (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
            .border(width = 1.dp, color = Color.Black, shape = CircleShape)
            .background(Color.White)
            .clickable {
                onClick()
            },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(model = category.imageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(45.dp))
        }
        Spacer(Modifier.height(5.dp))
        Text(category.name, style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp))
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProductGrid(categoryProductState: GetProductCategoryState,navController: NavController,vm:MainViewModel) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Flash Sale", style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = MontserratMedium
        ))
        Text("See more", style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = MontserratMedium,
            color = Color(232,144,142)),
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
                .clip(RoundedCornerShape(15.dp))

        )
        Card(
            onClick = {
                onClick.invoke()},
            modifier = Modifier
                .size(width = 143.dp, height = 160.dp)
                .padding(end = 8.dp, top = 8.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(15.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(250,249,253))
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(name, style = TextStyle(fontFamily = Montserrat), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.Bottom) {
                    Text("Rs: ", fontSize = 15.sp, color = Color(232,144,142))
                    Text(finalPrice, style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 21.sp, fontFamily = MontserratMedium, color = Color(232,144,142)))
                }
                Row(modifier = Modifier, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text("Rs: ", fontSize = 15.sp, color = Color.DarkGray)
                    Text(price, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 17.sp, fontFamily = MontserratMedium, color = Color.DarkGray), textDecoration = TextDecoration.LineThrough)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("20% off",style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, fontFamily = MontserratRegular, color = Color(232,144,142)))
                }
            }
        }
    }
}

