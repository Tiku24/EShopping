package com.example.eshopping.presentation.screen

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.eshopping.MainActivity
import com.example.eshopping.data.model.ShippingAddress
import com.example.eshopping.presentation.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ShippingScreenUI(image: String, name: String, price: String,vm: MainViewModel,auth: FirebaseAuth) {
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
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                SummaryRow(label = "Sub Total", amount = "Rs: $price")
                SummaryRow(label = "Shipping", amount = "Free")

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))


                SummaryRow(label = "Total", amount = "Rs: $price", isBold = true)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
//                // Contact Information
//                ContactInformation()
//                Spacer(modifier = Modifier.height(16.dp))

                // Shipping Address
                ShipAddress(vm,context, auth = auth)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipAddress(vm: MainViewModel,context: Context,auth: FirebaseAuth) {

    LaunchedEffect(Unit) {
        vm.getShippingAddress(auth.currentUser?.uid.toString())
    }

    val shipAddressState by vm.getShippingAddressState.collectAsState()
    val updateShipState by vm.updateShippingAddressState.collectAsState()
    val id = shipAddressState.success?.firstOrNull()?.id ?: ""
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    val shipState by vm.addShippingAddressState.collectAsState()
    var changeShip by remember { mutableStateOf(true) }
    var changeContact by remember { mutableStateOf(true) }

    if (!shipAddressState.success.isNullOrEmpty()) {
        LaunchedEffect(shipAddressState.success) {
            shipAddressState.success?.firstOrNull()?.let { addressData ->
                firstName = addressData.firstName ?: ""
                lastName = addressData.lastName ?: ""
                address = addressData.address ?: ""
                city = addressData.city ?: ""
                postalCode = addressData.postalCode ?: ""
                country = addressData.country ?: ""
                contactNumber = addressData.contactNumber ?: ""
            }
        }
    }

    when{
        updateShipState.data != null -> {
            Toast.makeText(context, updateShipState.data, Toast.LENGTH_SHORT).show()
            updateShipState.data=null
        }
    }


    when{
        shipState.success != null -> {
            Toast.makeText(context, shipState.success, Toast.LENGTH_SHORT).show()
            shipState.success=null
        }
        shipState.error != null -> {
            Toast.makeText(context, shipState.error, Toast.LENGTH_SHORT).show()
        }
    }
    Column {
        Text(
            text = "Shipping Address",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = country,
            readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeShip else !changeShip,
            onValueChange = {
                country = it },
            label = { Text("Country / Region") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(232, 144, 142),      // Border color when focused
                unfocusedBorderColor = Color(232, 144, 142),    // Border color when not focused
                disabledBorderColor = Color.LightGray,// Border color when disabled
                errorBorderColor = Color.Red,         // Border color in error state
                focusedLabelColor = Color.Gray,       // Label color when focused
                unfocusedLabelColor = Color.Black,     // Label color when not focused
                cursorColor = Color.Gray,             // Cursor color
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = firstName,
                readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeShip else !changeShip,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(232, 144, 142),      // Border color when focused
                    unfocusedBorderColor = Color(232, 144, 142),    // Border color when not focused
                    disabledBorderColor = Color.LightGray,// Border color when disabled
                    errorBorderColor = Color.Red,         // Border color in error state
                    focusedLabelColor = Color.Gray,       // Label color when focused
                    unfocusedLabelColor = Color.Black,     // Label color when not focused
                    cursorColor = Color.Gray,             // Cursor color
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = lastName,
                readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeShip else !changeShip,
                onValueChange = { lastName = it},
                label = { Text("Last Name") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(232, 144, 142),      // Border color when focused
                    unfocusedBorderColor = Color(232, 144, 142),    // Border color when not focused
                    disabledBorderColor = Color.LightGray,// Border color when disabled
                    errorBorderColor = Color.Red,         // Border color in error state
                    focusedLabelColor = Color.Gray,       // Label color when focused
                    unfocusedLabelColor = Color.Black,     // Label color when not focused
                    cursorColor = Color.Gray,             // Cursor color
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = address,
            readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeShip else !changeShip,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(232, 144, 142),      // Border color when focused
                unfocusedBorderColor = Color(232, 144, 142),    // Border color when not focused
                disabledBorderColor = Color.LightGray,// Border color when disabled
                errorBorderColor = Color.Red,         // Border color in error state
                focusedLabelColor = Color.Gray,       // Label color when focused
                unfocusedLabelColor = Color.Black,     // Label color when not focused
                cursorColor = Color.Gray,             // Cursor color
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = city,
                readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeShip else !changeShip,
                onValueChange = { city = it },
                label = { Text("City") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(232, 144, 142),      // Border color when focused
                    unfocusedBorderColor = Color(232, 144, 142),    // Border color when not focused
                    disabledBorderColor = Color.LightGray,// Border color when disabled
                    errorBorderColor = Color.Red,         // Border color in error state
                    focusedLabelColor = Color.Gray,       // Label color when focused
                    unfocusedLabelColor = Color.Black,     // Label color when not focused
                    cursorColor = Color.Gray,             // Cursor color
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = postalCode,
                readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeShip else !changeShip,
                onValueChange = { postalCode = it},
                label = { Text("Postal code") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(232, 144, 142),      // Border color when focused
                    unfocusedBorderColor = Color(232, 144, 142),    // Border color when not focused
                    disabledBorderColor = Color.LightGray,// Border color when disabled
                    errorBorderColor = Color.Red,         // Border color in error state
                    focusedLabelColor = Color.Gray,       // Label color when focused
                    unfocusedLabelColor = Color.Black,     // Label color when not focused
                    cursorColor = Color.Gray,             // Cursor color
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = contactNumber,
            readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeContact else !changeContact,
            onValueChange = { contactNumber = it },
            label = { Text("Contact number") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(232, 144, 142),      // Border color when focused
                unfocusedBorderColor = Color(232, 144, 142),    // Border color when not focused
                disabledBorderColor = Color.LightGray,// Border color when disabled
                errorBorderColor = Color.Red,         // Border color in error state
                focusedLabelColor = Color.Gray,       // Label color when focused
                unfocusedLabelColor = Color.Black,     // Label color when not focused
                cursorColor = Color.Gray,             // Cursor color
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Checkbox(
//                checked = isChecked,
//                modifier = Modifier
//                    .size(40.dp)  // Make it larger to emphasize roundness
//                    .clip(CircleShape),
//                onCheckedChange = {
//                    isChecked = !isChecked
//                    vm.addShippingAddress(ShippingAddress(firstName = firstName, lastName = lastName, contactNumber = contactNumber, address = address, city = city, postalCode = postalCode, country = country)) }
//            )
            RoundCheckbox(checked = isChecked,
                onCheckedChange = {
                    isChecked = !isChecked
                    vm.addShippingAddress(ShippingAddress(firstName = firstName, lastName = lastName, contactNumber = contactNumber, address = address, city = city, postalCode = postalCode, country = country))
                })
            Text(
                text = "Save this information for next time",
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(
                    width = 1.dp,
                    color = Color(232, 144, 142),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Contact")
                TextButton(onClick = {
                    changeContact = !changeContact
                    val updatedFields = mutableMapOf<String, Any?>()
                    if (changeContact){
                        if (contactNumber != shipAddressState.success?.get(0)?.contactNumber) updatedFields["contactNumber"] = contactNumber
                    if(updatedFields.isNotEmpty()) {
                        vm.updateShippingAddress(id,updatedFields)
                    }
                }
                },
                    enabled = !shipAddressState.success.isNullOrEmpty()
                ) {
                    Text(text = if(changeContact) "Change" else "Save")
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Ship to")
                TextButton(onClick = {
                    changeShip = !changeShip
                    val updatedFields = mutableMapOf<String, Any?>()
                    if (changeContact){
                    if (firstName != shipAddressState.success?.get(0)?.firstName) updatedFields["firstName"] = firstName
                    if (lastName != shipAddressState.success?.get(0)?.lastName) updatedFields["lastName"] = lastName
                    if (postalCode != shipAddressState.success?.get(0)?.postalCode) updatedFields["postalCode"] = postalCode
                    if (address != shipAddressState.success?.get(0)?.address) updatedFields["address"] = address
                    if (city != shipAddressState.success?.get(0)?.city) updatedFields["city"] = city
                    if (country != shipAddressState.success?.get(0)?.country) updatedFields["country"] = country
                    if(updatedFields.isNotEmpty()) {
                        vm.updateShippingAddress(id,updatedFields)
                    } }
                },
                    enabled = !shipAddressState.success.isNullOrEmpty()
                ) {
                    Text(text = if(changeShip) "Change" else "Save")
                }
            }
        }
    }
}

@Composable
fun ShippingMethod() {
    Spacer(modifier = Modifier.height(16.dp))
    Column(modifier = Modifier.border(width = 1.dp, color = Color(232, 144, 142), shape = RoundedCornerShape(10.dp))) {
        var isSelected = remember { mutableStateOf(true) }
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

@Composable
fun RoundCheckbox(checked: Boolean, onCheckedChange: () -> Unit) {

    Box(
        modifier = Modifier
            .size(20.dp) // Control overall size
            .clip(RoundedCornerShape(10.dp))
            .background(if (checked) Color(232, 144, 142) else Color(250, 234, 233))
            .clickable { checked },
        contentAlignment = Alignment.Center
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = {onCheckedChange()}, // Disable default click handling, handled by Box
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Transparent, // Hide default color
                uncheckedColor = Color.Transparent,
                checkmarkColor = Color.White // Set checkmark color
            ),
            modifier = Modifier.size(15.dp) // Reduce the size of the default checkbox
        )
    }
}
