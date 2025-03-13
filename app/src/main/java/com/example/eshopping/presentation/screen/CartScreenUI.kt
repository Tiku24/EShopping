package com.example.eshopping.presentation.screen

import android.graphics.Color.rgb
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.eshopping.data.model.Cart
import com.example.eshopping.presentation.navigation.Routes
import com.example.eshopping.presentation.viewmodel.MainViewModel
import com.example.eshopping.ui.theme.AppTheme
import com.example.eshopping.utils.getColorFromName
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CartScreenUI(vm:MainViewModel,navController: NavController) {
    val cartItemState by vm.getCartItemsState.collectAsState()
    val deleteItemState by vm.deleteCartItemState.collectAsState()

    LaunchedEffect(deleteItemState.data) {
        vm.getCartItems()
    }

    val subTotal = cartItemState.data?.sumOf { it.finalPrice.toInt() * it.quantity } ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(10.dp)
    ) {
        // Header
        Text(
            text = "Shopping Cart",
            color = AppTheme.colorScheme.primary,
            style = AppTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Continue Shopping
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable {
                    navController.navigate(Routes.HomeScreen){
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = null, modifier = Modifier.size(15.dp))
            Spacer(modifier = Modifier.width(5.dp))
            Text("Continue Shopping", style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(15.dp))

        Row {
            Text("Items", color = AppTheme.colorScheme.primary, style = AppTheme.typography.labelLarge)
            Spacer(modifier = Modifier.weight(1f))
            Text("Price",color = AppTheme.colorScheme.primary, style = AppTheme.typography.labelLarge)
            Spacer(modifier = Modifier.width(25.dp))
            Text("QTY",color = AppTheme.colorScheme.primary, style = AppTheme.typography.labelLarge)
            Spacer(modifier = Modifier.width(25.dp))
            Text("Total",color = AppTheme.colorScheme.primary, style = AppTheme.typography.labelLarge)
        }
        Spacer(modifier = Modifier.height(10.dp))
        // Items List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colorScheme.onPrimary)
        ) {
            items(cartItemState.data ?: emptyList(),key = { it.cId }) { item ->
                SwipeToDismissItem(item, onRemove = {vm.deleteCartItem(item.cId)},modifier = Modifier.animateItem(
                    fadeInSpec = null, fadeOutSpec = null, placementSpec = tween(200)
                )
                )
            }
            item {
                HorizontalDivider()
                // Subtotal
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Text(text = "Sub Total", style = AppTheme.typography.labelLarge,color = AppTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Rs: $subTotal", style = AppTheme.typography.paragraph)
                }

                // Checkout Button
                Button(
                    onClick = { navController.navigate(Routes.ShippingScreen(name = "", image = "", price = "", quantity = 0)) },
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 16.dp)
                ) {
                    Text(text = "Checkout", style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Composable
fun CartItemRow(cartData: Cart) {
    Column {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Item Image
        AsyncImage(
            model = cartData.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(AppTheme.shape.container)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Item Details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = cartData.name, style = AppTheme.typography.labelSmall)
            Text(text = "Size: ${cartData.sizes}", style = AppTheme.typography.cartSubTitle, color = AppTheme.colorScheme.primary)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Color: ", style = AppTheme.typography.cartSubTitle, color = AppTheme.colorScheme.primary)
                Box(
                    modifier = Modifier
                        .size(13.dp)
                        .background(getColorFromName(cartData.colors), shape = CircleShape)
                )
            }
        }

        // Price and Quantity
        Row(
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Rs:${cartData.price}", style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(25.dp))
            Text(text = "${cartData.quantity}", style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(25.dp))
            Text(text = "Rs ${cartData.finalPrice}",style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.primary)
        }
    }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissItem(
    cartData: Cart,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {state ->
            if (state == SwipeToDismissBoxValue.EndToStart){
                coroutineScope.launch {
                    delay(800)
                    onRemove()
                }
                true
            } else{
                false
            }
        },
        positionalThreshold = {it * .45f}
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val backgroundColor by animateColorAsState(
                targetValue = when(dismissState.currentValue){
                    SwipeToDismissBoxValue.StartToEnd -> Color.Green
                    SwipeToDismissBoxValue.EndToStart -> Color(rgb(216, 64, 64))
                    SwipeToDismissBoxValue.Settled -> Color.White
                }
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(16.dp),
                contentAlignment = Alignment.CenterEnd){
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete", tint = Color(rgb(244, 246, 255)))
            }
        },
        modifier = Modifier
    ) {
        CartItemRow(cartData)
    }
}