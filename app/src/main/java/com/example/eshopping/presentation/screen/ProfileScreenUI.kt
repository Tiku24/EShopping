package com.example.eshopping.presentation.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.eshopping.R
import com.example.eshopping.presentation.navigation.Routes
import com.example.eshopping.presentation.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileScreenUI(
    auth: FirebaseAuth,
    vm:MainViewModel,
    navController: NavController
) {

    LaunchedEffect(Unit) {
        vm.getUserById(auth.currentUser?.uid.toString())
    }

    var isEdit by remember { mutableStateOf(true) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current

    val updateState = vm.updateUserDataState.collectAsStateWithLifecycle()
    val userState = vm.getUserByIdState.collectAsStateWithLifecycle()
    Log.d("UserTAG", "ProfileScreenUI: $userState")

    LaunchedEffect(userState.value.success) {
        userState.value.success.let {
            firstName = it?.firstName.toString()
            lastName = it?.lastName.toString()
            email = it?.email.toString()
            address = it?.address.toString()
            phoneNumber = it?.phoneNumber.toString()
        }
    }

    when{
        updateState.value.success != null -> {
            Toast.makeText(context, updateState.value.success, Toast.LENGTH_SHORT).show()
        }
    }

    when {
        userState.value.isLoading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize())
        }

        userState.value.error != null -> {
            Text(userState.value.error.toString())
        }

        userState.value.success != null -> {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // Top Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(232, 144, 142))
                        .padding(vertical = 30.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }){ Icon(imageVector = Icons.Filled.ArrowBackIosNew,contentDescription = null, tint = Color.White) }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Picture with Edit Icon
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Image(
                            painter = painterResource(R.drawable.frock), // Replace with your profile image resource
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape)
                        )
                        IconButton(
                            onClick = { /* Handle edit picture */ },
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White, shape = CircleShape)
                                .border(1.dp, Color.Gray, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Picture",
                                tint = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Input Fields
                    InputField(label = "First name", value = firstName, icon = Icons.Default.Person, onValueChange = {
                        firstName = it
                    },
                        isEditable = isEdit)
                    Spacer(modifier = Modifier.height(16.dp))
                    InputField(
                        label = "Last name",
                        value = lastName,
                        icon = Icons.Default.Person,
                        onValueChange = {
                            lastName = it
                        },
                        isEditable = isEdit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    InputField(
                        label = "Email address",
                        value = email,
                        icon = Icons.Default.Email,
                        onValueChange = {
                            email = it
                        },
                        isEditable = isEdit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    InputField(
                        label = "Address",
                        value = address,
                        icon = Icons.Default.LocationOn,
                        onValueChange = {
                            address = it
                        },
                        isEditable = isEdit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    InputField(
                        label = "phoneNumber",
                        value = phoneNumber,
                        icon = Icons.Default.Call,
                        onValueChange = {
                            phoneNumber = it
                        },
                        isEditable = isEdit
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Save Changes Button
                    Button(
                        onClick = {
                            val updatedFields = mutableMapOf<String, Any?>()

                            if (firstName != userState.value.success?.firstName) updatedFields["firstName"] = firstName
                            if (lastName != userState.value.success?.lastName) updatedFields["lastName"] = lastName
                            if (email != userState.value.success?.email) updatedFields["email"] = email
                            if (address != userState.value.success?.address) updatedFields["address"] = address
                            if (phoneNumber != userState.value.success?.phoneNumber) updatedFields["phoneNumber"] = phoneNumber

                            if(updatedFields.isNotEmpty()) {
                                vm.updateUser(updatedFields)
                            }
                            isEdit = !isEdit
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(232, 144, 142))
                    ) {
                        Text(if (isEdit) "Edit" else "Save changes", color = Color.White)
                    }

                    Button(onClick = {
                        vm.resetState()
                        auth.signOut()
                        navController.navigate(Routes.SignInScreen) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(232, 144, 142))) {
                        Text(text = "Sign Out")
                    }
                }
            }
        }
    }
}

@Composable
fun InputField(label: String, value: String, icon: ImageVector,onValueChange: (String) -> Unit,isEditable:Boolean) {
    OutlinedTextField(
        value = value,
        onValueChange = {onValueChange(it)},
        label = { Text(label) },
        readOnly = isEditable,
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth()
    )
}