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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.eshopping.MainActivity
import com.example.eshopping.data.model.Cart
import com.example.eshopping.data.model.ShippingAddress
import com.example.eshopping.presentation.navigation.Routes
import com.example.eshopping.presentation.viewmodel.MainViewModel
import com.example.eshopping.ui.theme.AppTheme
import com.example.eshopping.utils.getColorFromName
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ShippingScreenUI(image: String, name: String, price: String, quantity: Int,vm: MainViewModel,auth: FirebaseAuth,navController: NavController) {
    val scrollState = rememberScrollState()
    val context= LocalContext.current
    val activity = context as? MainActivity
    val numericPrice = price.filter { it.isDigit() }.toIntOrNull() ?: 0
    vm.totalAmount.value = numericPrice * quantity
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    )
            {
                Text(
                    text = "Shipping",
                    style = AppTheme.typography.titleLarge,
                    color = AppTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Product Details
                ProductDetails(name,vm,image,navController)
                Spacer(modifier = Modifier.height(16.dp))


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
                    style = AppTheme.typography.labelLarge,
                    color = AppTheme.colorScheme.primary
                )
                ShippingMethod()
                Spacer(modifier = Modifier.height(16.dp))

                // Continue to Shipping Button
                Button(
                    onClick = { activity?.startPayment(
                        name = name,
                        amount = vm.totalAmount.value
                    ) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colorScheme.primary, contentColor = AppTheme.colorScheme.onPrimary)
                ) {
                    Text(text = "Continue to Shipping", style = AppTheme.typography.labelNormal)
                }
            }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProductDetails(
    name: String,
    vm: MainViewModel,
    image: String,
    navController: NavController
) {
    TrackCurrentRoute(navController)
    val previousRoute = navController.previousBackStackEntry?.destination?.route
    when(previousRoute) {
        Routes.CartScreen::class.qualifiedName -> {
            SelectListOfProduct(vm)
        }
        else -> {
            SelectProduct(name,image,vm)
        }
    }
}

@Composable
fun SummaryRow(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.primary)
        Text(text = amount, style = AppTheme.typography.labelRow, color = AppTheme.colorScheme.primary)
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
                firstName = addressData.firstName
                lastName = addressData.lastName
                address = addressData.address
                city = addressData.city
                postalCode = addressData.postalCode
                country = addressData.country
                contactNumber = addressData.contactNumber
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
            style = AppTheme.typography.labelLarge,
            color = AppTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = country,
            textStyle = AppTheme.typography.labelLarge,
            readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeShip else !changeShip,
            onValueChange = {
                country = it },
            label = { Text("Country / Region", style = AppTheme.typography.labelNormal) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                cursorColor = Color.Gray,             // Cursor color
                selectionColors = LocalTextSelectionColors.current,
                focusedBorderColor = AppTheme.colorScheme.primary,      // Border color when focused
                unfocusedBorderColor = AppTheme.colorScheme.primary,    // Border color when not focused
                disabledBorderColor = Color.LightGray,// Border color when disabled
                errorBorderColor = Color.Red,         // Border color in error state
                focusedLabelColor = AppTheme.colorScheme.primary,       // Label color when focused
                unfocusedLabelColor = Color.Black,     // Label color when not focused

            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = firstName,
                textStyle = AppTheme.typography.labelLarge,
                readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeShip else !changeShip,
                onValueChange = { firstName = it },
                label = { Text("First Name",style = AppTheme.typography.labelNormal) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    cursorColor = Color.Gray,             // Cursor color
                    selectionColors = LocalTextSelectionColors.current,
                    focusedBorderColor = AppTheme.colorScheme.primary,      // Border color when focused
                    unfocusedBorderColor = AppTheme.colorScheme.primary,    // Border color when not focused
                    disabledBorderColor = Color.LightGray,// Border color when disabled
                    errorBorderColor = Color.Red,         // Border color in error state
                    focusedLabelColor = AppTheme.colorScheme.primary,       // Label color when focused
                    unfocusedLabelColor = Color.Black,     // Label color when not focused
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = lastName,
                textStyle = AppTheme.typography.labelLarge,
                readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeShip else !changeShip,
                onValueChange = { lastName = it},
                label = { Text("Last Name",style = AppTheme.typography.labelNormal) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    cursorColor = Color.Gray,             // Cursor color
                    selectionColors = LocalTextSelectionColors.current,
                    focusedBorderColor = AppTheme.colorScheme.primary,      // Border color when focused
                    unfocusedBorderColor = AppTheme.colorScheme.primary,    // Border color when not focused
                    disabledBorderColor = Color.LightGray,// Border color when disabled
                    errorBorderColor = Color.Red,         // Border color in error state
                    focusedLabelColor = AppTheme.colorScheme.primary,       // Label color when focused
                    unfocusedLabelColor = Color.Black,     // Label color when not focused
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = address,
            textStyle = AppTheme.typography.labelLarge,
            readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeShip else !changeShip,
            onValueChange = { address = it },
            label = { Text("Address",style = AppTheme.typography.labelNormal) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                cursorColor = Color.Gray,             // Cursor color
                selectionColors = LocalTextSelectionColors.current,
                focusedBorderColor = AppTheme.colorScheme.primary,      // Border color when focused
                unfocusedBorderColor = AppTheme.colorScheme.primary,    // Border color when not focused
                disabledBorderColor = Color.LightGray,// Border color when disabled
                errorBorderColor = Color.Red,         // Border color in error state
                focusedLabelColor = AppTheme.colorScheme.primary,       // Label color when focused
                unfocusedLabelColor = Color.Black,     // Label color when not focused
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = city,
                textStyle = AppTheme.typography.labelLarge,
                readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeShip else !changeShip,
                onValueChange = { city = it },
                label = { Text("City",style = AppTheme.typography.labelNormal) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    cursorColor = Color.Gray,             // Cursor color
                    selectionColors = LocalTextSelectionColors.current,
                    focusedBorderColor = AppTheme.colorScheme.primary,      // Border color when focused
                    unfocusedBorderColor = AppTheme.colorScheme.primary,    // Border color when not focused
                    disabledBorderColor = Color.LightGray,// Border color when disabled
                    errorBorderColor = Color.Red,         // Border color in error state
                    focusedLabelColor = AppTheme.colorScheme.primary,       // Label color when focused
                    unfocusedLabelColor = Color.Black,     // Label color when not focused
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = postalCode,
                textStyle = AppTheme.typography.labelLarge,
                readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeShip else !changeShip,
                onValueChange = { postalCode = it},
                label = { Text("Postal code",style = AppTheme.typography.labelNormal) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    cursorColor = Color.Gray,             // Cursor color
                    selectionColors = LocalTextSelectionColors.current,
                    focusedBorderColor = AppTheme.colorScheme.primary,      // Border color when focused
                    unfocusedBorderColor = AppTheme.colorScheme.primary,    // Border color when not focused
                    disabledBorderColor = Color.LightGray,// Border color when disabled
                    errorBorderColor = Color.Red,         // Border color in error state
                    focusedLabelColor = AppTheme.colorScheme.primary,       // Label color when focused
                    unfocusedLabelColor = Color.Black,     // Label color when not focused
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = contactNumber,
            textStyle = AppTheme.typography.labelLarge,
            readOnly = if (!shipAddressState.success.isNullOrEmpty()) changeContact else !changeContact,
            onValueChange = { contactNumber = it },
            label = { Text("Contact number",style = AppTheme.typography.labelNormal) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                cursorColor = Color.Gray,             // Cursor color
                selectionColors = LocalTextSelectionColors.current,
                focusedBorderColor = AppTheme.colorScheme.primary,      // Border color when focused
                unfocusedBorderColor = AppTheme.colorScheme.primary,    // Border color when not focused
                disabledBorderColor = Color.LightGray,// Border color when disabled
                errorBorderColor = Color.Red,         // Border color in error state
                focusedLabelColor = AppTheme.colorScheme.primary,       // Label color when focused
                unfocusedLabelColor = Color.Black,     // Label color when not focused
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
                style = AppTheme.typography.labelNormal,
                color = AppTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = AppTheme.colorScheme.primary,
                    shape = AppTheme.shape.container
                )
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Contact", style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.primary)
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
                    Text(text = if(changeContact) "Change" else "Save",style = AppTheme.typography.labelSmall, color = AppTheme.colorScheme.primary)
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Ship to",style = AppTheme.typography.labelNormal, color = AppTheme.colorScheme.primary)
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
                    Text(text = if(changeShip) "Change" else "Save",style = AppTheme.typography.labelSmall, color = AppTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun ShippingMethod() {
    Spacer(modifier = Modifier.height(16.dp))
    Column(modifier = Modifier.border(width = 1.dp, color = AppTheme.colorScheme.primary, shape = AppTheme.shape.container)) {
        var isSelected = remember { mutableStateOf(true) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = isSelected.value,
                onClick = { isSelected.value=true },
                colors = RadioButtonColors(selectedColor = AppTheme.colorScheme.primary, unselectedColor = AppTheme.colorScheme.primary, disabledSelectedColor = AppTheme.colorScheme.primary, disabledUnselectedColor = AppTheme.colorScheme.primary)
            )
            Text(
                text = "Standard FREE delivery over Rs:4500(Free)",
                style = AppTheme.typography.labelNormal,
                color = AppTheme.colorScheme.primary
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
                style = AppTheme.typography.labelNormal,
                color = AppTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun RoundCheckbox(checked: Boolean, onCheckedChange: () -> Unit) {

    Box(
        modifier = Modifier
            .size(20.dp) // Control overall size
            .clip(AppTheme.shape.container)
            .background(if (checked) AppTheme.colorScheme.primary else Color(250, 234, 233))
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


@Composable
fun TrackCurrentRoute(navController: NavController) {
    val previousRoute = navController.previousBackStackEntry?.destination?.route

    LaunchedEffect(previousRoute) {
        Log.d("NavigationTracker", "Current Route: $previousRoute")
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SelectProduct(name: String, image: String, vm: MainViewModel) {
    Column {
        val selectedSize by vm.selectedSize.collectAsState()
        val selectedColor by vm.selectedColor.collectAsState()
        val totalPrice = vm.totalAmount.value
        Row(verticalAlignment = Alignment.Top) {
            AsyncImage(
                model = image, contentDescription = null, modifier = Modifier
                    .size(width = 62.dp, height = 80.dp)
                    .clip(
                        RoundedCornerShape(13.dp)
                    )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = AppTheme.typography.labelLarge,
                    color = AppTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = selectedSize,
                        style = AppTheme.typography.paragraph,
                        color = AppTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                getColorFromName(selectedColor),
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = AppTheme.colorScheme.primary,
                                CircleShape
                            )
                    )
                }
            }
        }
        // Order Summary
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        SummaryRow(label = "Sub Total", amount = "Rs: $totalPrice")
        SummaryRow(label = "Shipping", amount = "Free")

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))


        SummaryRow(label = "Total", amount = "Rs: $totalPrice")
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Black)
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SelectListOfProduct(vm: MainViewModel) {
    val cartState by vm.getCartItemsState.collectAsState()
    vm.totalAmount.value = cartState.data?.sumOf { it.finalPrice.toInt() * it.quantity } ?: 0
    Column {
        cartState.data?.forEach { data ->
            CartProduct(data)
        }

        // Order Summary
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        SummaryRow(label = "Sub Total", amount = "Rs: ${vm.totalAmount.value}")
        SummaryRow(label = "Shipping", amount = "Free")

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))


        SummaryRow(label = "Total", amount = "Rs: ${vm.totalAmount.value}")
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Black)
    }

}

@Composable
fun CartProduct(cartData: Cart) {
    Column(Modifier.padding(vertical = 10.dp)) {
        Row(verticalAlignment = Alignment.Top) {
            AsyncImage(
                model = cartData.image, contentDescription = null, modifier = Modifier
                    .size(width = 62.dp, height = 80.dp)
                    .clip(
                        RoundedCornerShape(13.dp)
                    )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartData.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = cartData.colors,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                getColorFromName(cartData.colors),
                                shape = CircleShape
                            )
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
