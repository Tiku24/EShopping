package com.example.eshopping.presentation.screen

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.eshopping.data.model.UserData
import com.example.eshopping.presentation.navigation.Routes
import com.example.eshopping.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenUI(modifier: Modifier = Modifier,vm: MainViewModel= hiltViewModel(),navController: NavController) {
    val state by vm.registerUserWithEmailPassState.collectAsState()
    val context = LocalContext.current

    when{
        state.isLoading -> {
            CircularProgressIndicator(
                modifier
                    .fillMaxSize()
                    .wrapContentSize())
        }
        state.error != null -> {
            Text(state.error.toString())
        }
        state.success != null -> {
            Toast.makeText(context, state.success, Toast.LENGTH_SHORT).show()
            state.success = null
            navController.navigate(Routes.HomeScreen)
        }
    }


        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var address by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var imageUrl by remember { mutableStateOf("") }

    Box(
        modifier = modifier // Background color from the screenshot
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                OutlinedTextField(value = firstName, onValueChange = {firstName = it}, label = {Text("First Name")},colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(232,144,142)
                ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp))
                OutlinedTextField(value = lastName, onValueChange = {lastName = it},label = {Text("Last Name")},colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(232,144,142)
                ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp))
                OutlinedTextField(value = email, onValueChange = {email = it},label = {Text("Email")},colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(232,144,142)
                ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp))
                OutlinedTextField(value = password, onValueChange = {password = it},label = {Text("Password")},colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(232,144,142)
                ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp))
                OutlinedTextField(value = address, onValueChange = {address = it},label = {Text("Address")},colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(232,144,142)
                ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp))
                OutlinedTextField(value = phone, onValueChange = {phone = it},label = {Text("Phone Number")},colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(232,144,142)
                ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp))

                Button(onClick = {
                    vm.registerUserWithEmailPass(UserData(firstName = firstName, lastName = lastName, email = email, password = password, address = address, phoneNumber = phone, image = imageUrl))
                },
                    colors = ButtonDefaults.buttonColors(Color(232,144,142)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp))
                {
                    Text(text = "Sign Up",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Sign Up Option
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Sign In",
                        color = Color(232,144,142),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.SignInScreen) // Adjust navigation logic
                        }
                    )
                }
            }
        }
    }


}