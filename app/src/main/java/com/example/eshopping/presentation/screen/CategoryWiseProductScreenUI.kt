package com.example.eshopping.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.eshopping.data.model.Product
import com.example.eshopping.presentation.navigation.Routes
import com.example.eshopping.presentation.viewmodel.MainViewModel

@Composable
fun ItemRow(productData: Product,onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { onClick() },
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AsyncImage(
                model = productData.image,
                contentDescription = "null",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .height(140.dp)
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(15.dp))
                )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = productData.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = productData.price, fontSize = 14.sp, textDecoration = TextDecoration.LineThrough)
                Text(text = productData.finalPrice, color = Color(232,144,142))
            }
        }
    }
}

@Composable
fun CategoryWiseProductScreenUI(vm: MainViewModel,navController: NavController) {
    val categoryProductState by vm.getProductCategoryState.collectAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)
    ) {
        Row(modifier = Modifier.clickable {
            navController.navigateUp()
        },
            verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "", tint = Color.Black, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(5.dp))
            Text("See Your Favourite", fontSize = 15.sp, color = Color.Black)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Items", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(categoryProductState.productData ?: emptyList()) { item ->
                ItemRow(item, onClick = { navController.navigate(Routes.ProductDetailScreen(pId = item.pId))})
            }
        }
    }
}
