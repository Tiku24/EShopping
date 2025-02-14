package com.example.eshopping.presentation.screen

import android.annotation.SuppressLint
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.eshopping.MainActivity
import com.example.eshopping.presentation.viewmodel.MainViewModel


@Composable
fun ShippingScreenUI(image: String, name: String, price: String,vm: MainViewModel) {
    val scrollState = rememberScrollState()
    val context= LocalContext.current
    val activity = context as? MainActivity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    )
            {
                Text(
                    text = "Shipping",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Product Details
                ProductDetails(name,vm,image)
                Spacer(modifier = Modifier.height(16.dp))

                // Order Summary
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                SummaryRow(label = "Sub Total", amount = "Rs: $price")
                SummaryRow(label = "Shipping", amount = "Free")

                Divider(modifier = Modifier.padding(vertical = 8.dp))


                SummaryRow(label = "Total", amount = "Rs: $price", isBold = true)
                Spacer(modifier = Modifier.height(16.dp))

                // Contact Information
                ContactInformation()
                Spacer(modifier = Modifier.height(16.dp))

                // Shipping Address
                ShippingAddress()
                Spacer(modifier = Modifier.height(16.dp))

                // Shipping Method
                Text(
                    text = "Shipping Method",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                ShippingMethod()
                Spacer(modifier = Modifier.height(16.dp))

                // Continue to Shipping Button
                Button(
                    onClick = { activity?.startPayment(
                        name = name,
                        amount = price
                    ) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(232, 144, 142))
                ) {
                    Text(text = "Continue to Shipping")
                }
            }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProductDetails(name: String,vm: MainViewModel,image: String) {
    Column {
        val selectedSize by vm.selectedSize.collectAsState()
        val selectedColor by vm.selectedColor.collectAsState()

        Row(verticalAlignment = Alignment.Top) {
            AsyncImage(model = image,contentDescription = null, modifier = Modifier
                .size(width = 62.dp, height = 80.dp)
                .clip(
                    RoundedCornerShape(13.dp)
                ))
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = selectedSize,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
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
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(getColorFromName(selectedColor), shape = CircleShape)
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                CircleShape
                            )
                    )
                }
            }
        }

    }
}

@Composable
fun SummaryRow(label: String, amount: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        Text(text = amount, fontSize = 14.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun ContactInformation() {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Contact Information",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Already have an account? Login",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { /* Handle email input */ },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ShippingAddress() {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { /* Handle country/region input */ },
            label = { Text("Country / Region") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = "",
                onValueChange = { /* Handle first name input */ },
                label = { Text("First Name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = "",
                onValueChange = { /* Handle last name input */ },
                label = { Text("Last Name") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { /* Handle address input */ },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = "",
                onValueChange = { /* Handle city input */ },
                label = { Text("City") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = "",
                onValueChange = { /* Handle postal code input */ },
                label = { Text("Postal code") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = { /* Handle contact number input */ },
            label = { Text("Contact number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = false,
                onCheckedChange = { /* Handle save info checkbox */ }
            )
            Text(
                text = "Save this information for next time",
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { /* Handle contact change */ }) {
                Text(text = "Contact    Change")
            }
            TextButton(onClick = { /* Handle ship to change */ }) {
                Text(text = "Ship to    Change")
            }
        }
    }
}

@Composable
fun ShippingMethod() {
    Column(modifier = Modifier.border(width = 1.dp, color = Color(232, 144, 142), shape = RoundedCornerShape(10.dp))) {
        var isSelected = remember { mutableStateOf(true) }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = isSelected.value,
                onClick = { isSelected.value=true }
            )
            Text(
                text = "Standard FREE delivery over Rs:4500(Free)",
                fontSize = 12.sp
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = !isSelected.value,
                onClick = { isSelected.value=false }
            )
            Text(
                text = "Cash on delivery over Rs:4500 (Free Delivery, COD processing fee 100 only)",
                fontSize = 12.sp
            )
        }
    }
}