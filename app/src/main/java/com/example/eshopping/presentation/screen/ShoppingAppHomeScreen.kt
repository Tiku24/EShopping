package com.example.eshopping.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.eshopping.R
import com.example.eshopping.data.model.Category
import com.example.eshopping.presentation.viewmodel.GetProductCategoryState
import com.example.eshopping.ui.theme.Montserrat
import com.example.eshopping.ui.theme.MontserratMedium
import com.example.eshopping.ui.theme.MontserratRegular


@Composable
fun ShoppingAppHomeScreen(modifier: Modifier, categoryProductState: GetProductCategoryState) {
    Column(
        modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        SearchBar()
        CategorySection(categoryProductState)
        ProductGrid(categoryProductState)
    }
}

@Composable
fun SearchBar() {
    BasicTextField(
        value = "",
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 8.dp)
            .border(width = 1.dp, color = Color(232,144,142), shape = RoundedCornerShape(15.dp))
            .background(Color(250,249,253), shape = RoundedCornerShape(15.dp)),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_search),
                    contentDescription = "Search Icon",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                if ("".isEmpty()) {
                    Text(
                        text = "Search products...",
                        style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                    )
                }
                innerTextField()
            }
        }
    )
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun CategorySection(categoryProductState: GetProductCategoryState) {
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
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categoryProductState.categoryData ?: emptyList()){
                CategoryChip(it)
            }
        }
    }
}

@Composable
fun CategoryChip(category: Category) {
//    Button(
//        onClick = {},
//        shape = RoundedCornerShape(30),
//        colors = ButtonDefaults.buttonColors(containerColor = Color(232,144,142))
//    ) {
//        Text(text = category.name, color = Color.White)
//    }
    Column(modifier = Modifier.padding(horizontal =12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(70.dp).border(width = 1.dp, color = Color.Black, shape = CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
            AsyncImage(model = category.imageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(45.dp))
        }
        Text(category.name, style = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp))
    }
}

@Composable
fun ProductGrid(categoryProductState: GetProductCategoryState) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Flash Sale", style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = MontserratMedium
        ))
        Text("See more", style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = MontserratMedium,
            color = Color(232,144,142)
        ))
    }
    LazyRow {
        items(categoryProductState.productData ?: emptyList()){
            ProductItem(name = it.name, price = it.price, finalPrice = it.finalPrice, imageUrl = it.image)
        }
    }

}

@Composable
fun ProductItem(imageUrl:String,name:String,price:String,finalPrice:String) {
    Column {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.width (145.dp).height(190.dp).padding(end = 8.dp)
        )
        Card(
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

