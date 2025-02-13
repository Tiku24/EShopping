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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.eshopping.presentation.navigation.Routes
import com.example.eshopping.presentation.viewmodel.GetSpecificProductState
import com.example.eshopping.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun ProductScreenUI(
    id: String,
    modifier: Modifier = Modifier,
    vm: MainViewModel,
    navController: NavController
) {
    val state = vm.getSpecificProductState.collectAsState()

    LaunchedEffect(Unit) {
        vm.getSpecificProduct(id = id)
    }

    when {
        state.value.success != null -> {
            ProductDetailScreen(state, navController,vm)
        }

        state.value.isLoading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.value.error != null -> {
            Text(state.value.error.toString())
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProductDetailScreen(state: State<GetSpecificProductState>, navController: NavController,vm: MainViewModel) {
    val data = state.value.success
    val color = data?.colors
    val size = data?.sizes
    val availableUnits = data?.availableUnits
    val quantity = remember { mutableStateOf(1) }
    var selectedColor =vm.selectedColor.collectAsState()
    val selectedSize = vm.selectedSize.collectAsState()


    Log.d("QAU", "${quantity.value}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Image Section
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = data!!.image, // Replace with actual image
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        Icons.Filled.FavoriteBorder,
                        contentDescription = "Save",
                        tint = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.4f)) // Semi-transparent background for text
                    .padding(horizontal = 8.dp, vertical = 20.dp)
            ) {
                Text(
                    text = data.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Product Information Section
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(4) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.btn_star_big_on),
                        contentDescription = "Star",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Icon(
                    painter = painterResource(id = android.R.drawable.btn_star_big_off),
                    contentDescription = "Star",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Rs: ${data?.finalPrice}",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Size Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Size",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                )
                Row(
                    modifier = Modifier
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Decrease Button
                    IconButton(onClick = {
                        if (quantity.value > 1) {
                            quantity.value--
                        }
                    }) {
                        Icon(
                            Icons.Filled.Remove,
                            contentDescription = "Decrease Quantity",
                            tint = Color.Black
                        )
                    }

                    // Quantity Text
                    Text(
                        text = quantity.value.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 20.sp
                        ),
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )

                    // Increase Button
                    IconButton(onClick = {
                        if (quantity.value < availableUnits!!) {
                            quantity.value++
                        }
                    }) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Increase Quantity",
                            tint = Color.Black
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                size?.forEach { s ->
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .border(
                                width = if (selectedSize.value == s) 2.dp else 1.dp,
                                color = if (selectedSize.value == s) Color.Black else Color.Gray,
                                RoundedCornerShape(8.dp)
                            )
                            .background(Color.White)
                            .clickable {
                                vm.updateSelectedSize(s)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = s, style = TextStyle(fontSize = 14.sp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Color Section
            Text(
                text = "Color",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                val colorMap = mapOf(
                    "red" to Color.Red,
                    "blue" to Color.Blue,
                    "black" to Color.Black,
                    "white" to Color.White,
                    "green" to Color.Green,
                    "yellow" to Color.Yellow,
                    "gray" to Color.Gray,
                    "cyan" to Color.Cyan
                )
                fun getColorFromName(colorName: String): Color {
                    return colorMap[colorName] ?: Color.Gray // Default to Gray if not found
                }
                val context =LocalContext.current

                color?.forEach { clr ->
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(getColorFromName(clr), shape = CircleShape)
                            .border(
                                width = if (selectedColor.value == clr) 2.dp else 1.dp,
                                color = if (selectedColor.value == clr) Color.Black else Color.Gray,
                                CircleShape
                            )
                            .clickable {
                                vm.updateSelectedColor(clr)
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Specification Section
            Text(
                text = "Specification",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Dress\nMaterial: Linen\nMaterial Composition: 100% Linen",
                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please bear in mind that the photo may be slightly different from the actual item in terms of color due to lighting conditions or the display used to view.",
                style = TextStyle(fontSize = 12.sp, color = Color.Gray),
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Button(
                onClick = {
                    navController.navigate(Routes.ShippingScreen(name = data!!.name, image = data.image, price = data.finalPrice))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(232, 144, 142))
            ) {
                Text(text = "Buy now", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* Add to cart action */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text(text = "Add to Cart", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



