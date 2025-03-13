package com.example.eshopping.presentation.screen

//noinspection SuspiciousImport
import android.R
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.eshopping.data.model.Cart
import com.example.eshopping.presentation.navigation.Routes
import com.example.eshopping.presentation.viewmodel.GetSpecificProductState
import com.example.eshopping.presentation.viewmodel.MainViewModel
import com.example.eshopping.ui.theme.AppTheme
import com.example.eshopping.utils.getColorFromName

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
                CircularProgressIndicator(color = AppTheme.colorScheme.primary)
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
    val checkoutState = vm.addToCartState.collectAsState()
    val context = LocalContext.current

    when{
        checkoutState.value.data != null-> {
            Toast.makeText(context, checkoutState.value.data, Toast.LENGTH_SHORT).show()
            checkoutState.value.data=null
        }
    }

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
                IconButton(onClick = { navController.popBackStack() }, colors = IconButtonColors(
                    contentColor = AppTheme.colorScheme.primary,
                    containerColor = AppTheme.colorScheme.onPrimary,
                    disabledContainerColor = AppTheme.colorScheme.primary,
                    disabledContentColor = AppTheme.colorScheme.primary
                )) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AppTheme.colorScheme.primary
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
                    style = AppTheme.typography.titleNormal,
                    color = AppTheme.colorScheme.onPrimary,
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
                        painter = painterResource(id = R.drawable.btn_star_big_on),
                        contentDescription = "Star",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.btn_star_big_off),
                    contentDescription = "Star",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Rs: ${data?.finalPrice}",
                style = AppTheme.typography.titleNormal,
                color = AppTheme.colorScheme.primary
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
                    style = AppTheme.typography.labelLarge,
                    color = AppTheme.colorScheme.primary
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
                            tint = AppTheme.colorScheme.primary
                        )
                    }

                    // Quantity Text
                    Text(
                        text = quantity.value.toString(),
                        style = AppTheme.typography.titleNormal,
                        color = AppTheme.colorScheme.primary,
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
                            tint = AppTheme.colorScheme.primary
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
                        Text(text = s, style = AppTheme.typography.paragraph)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Color Section
            Text(
                text = "Color",
                style = AppTheme.typography.labelLarge,
                color = AppTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

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
                style = AppTheme.typography.labelLarge,
                color = AppTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Dress\nMaterial: Linen\nMaterial Composition: 100% Linen",
                style = AppTheme.typography.labelNormal,
                color = AppTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please bear in mind that the photo may be slightly different from the actual item in terms of color due to lighting conditions or the display used to view.",
                style = AppTheme.typography.labelNormal,
                color = AppTheme.colorScheme.secondary,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Button(
                onClick = {
                    navController.navigate(Routes.ShippingScreen(name = data!!.name, image = data.image, price = data.finalPrice, quantity = quantity.value))
                },
                modifier = Modifier.fillMaxWidth(),
                shape = AppTheme.shape.button,
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colorScheme.primary, contentColor = AppTheme.colorScheme.onPrimary)
            ) {
                Text(text = "Buy Now",style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.onPrimary)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    vm.addToCart(Cart(
                    name = data!!.name,
                    image = data.image,
                    price = data.price,
                    finalPrice = data.finalPrice,
                    sizes = selectedSize.value,
                    colors = selectedColor.value,
                    quantity = quantity.value,
                    description = data.description,
                    category = data.category,
                )) },
                modifier = Modifier.fillMaxWidth(),
                shape = AppTheme.shape.button,
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colorScheme.primary, contentColor = AppTheme.colorScheme.onPrimary)
            ) {
                Text(text = "Add to Cart", style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.onPrimary)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



