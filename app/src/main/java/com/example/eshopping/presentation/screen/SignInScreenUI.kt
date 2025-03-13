package com.example.eshopping.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eshopping.R
import com.example.eshopping.presentation.navigation.Routes
import com.example.eshopping.presentation.viewmodel.MainViewModel
import com.example.eshopping.ui.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreenUI(modifier: Modifier = Modifier,vm: MainViewModel,navController: NavController) {
    val context = LocalContext.current
    val state by vm.signInUserWithEmailPassState.collectAsState()

    when{
        state.isLoading -> {
            CircularProgressIndicator(
                modifier
                    .fillMaxSize()
                    .wrapContentSize(),color = AppTheme.colorScheme.primary)
        }
        state.success != null -> {
            Toast.makeText(context, state.success, Toast.LENGTH_SHORT).show()
            state.success = null
            navController.navigate(Routes.HomeScreen) {
                popUpTo(Routes.SignInScreen){
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
        state.error != null -> {
            Text(state.error.toString())
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

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
                Image(
                    painter = painterResource(id = R.drawable.shopping), // Replace with your icon resource
                    contentDescription = "Logo",
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = "EShopping",
                    style = AppTheme.typography.titleLarge,
                    color = AppTheme.colorScheme.priceColor
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign In Text
            Text(
                text = "Sign in",
                style = AppTheme.typography.titleNormal,
                color = AppTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = email,
                textStyle = AppTheme.typography.labelLarge,
                onValueChange = {email = it},
                placeholder = { Text(text = "Email", color = AppTheme.colorScheme.primary,style = AppTheme.typography.labelNormal) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = AppTheme.colorScheme.primary) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = OutlinedTextFieldTokens.FocusInputColor.value,
                    unfocusedTextColor = OutlinedTextFieldTokens.InputColor.value,
                    disabledTextColor = OutlinedTextFieldTokens.DisabledInputColor.value
                        .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                    errorTextColor = OutlinedTextFieldTokens.ErrorInputColor.value,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    cursorColor = Color.Gray,             // Cursor color
                    errorCursorColor = OutlinedTextFieldTokens.ErrorFocusCaretColor.value,
                    selectionColors = LocalTextSelectionColors.current,
                    focusedBorderColor = AppTheme.colorScheme.primary,      // Border color when focused
                    unfocusedBorderColor = AppTheme.colorScheme.primary,    // Border color when not focused
                    disabledBorderColor = Color.LightGray,// Border color when disabled
                    errorBorderColor = Color.Red,         // Border color in error state
                    focusedLeadingIconColor = OutlinedTextFieldTokens.FocusLeadingIconColor.value,
                    unfocusedLeadingIconColor = OutlinedTextFieldTokens.LeadingIconColor.value,
                    disabledLeadingIconColor = OutlinedTextFieldTokens.DisabledLeadingIconColor.value
                        .copy(alpha = OutlinedTextFieldTokens.DisabledLeadingIconOpacity),
                    errorLeadingIconColor = OutlinedTextFieldTokens.ErrorLeadingIconColor.value,
                    focusedTrailingIconColor = OutlinedTextFieldTokens.FocusTrailingIconColor.value,
                    unfocusedTrailingIconColor = OutlinedTextFieldTokens.TrailingIconColor.value,
                    disabledTrailingIconColor = OutlinedTextFieldTokens.DisabledTrailingIconColor
                        .value.copy(alpha = OutlinedTextFieldTokens.DisabledTrailingIconOpacity),
                    errorTrailingIconColor = OutlinedTextFieldTokens.ErrorTrailingIconColor.value,
                    focusedLabelColor = AppTheme.colorScheme.primary,       // Label color when focused
                    unfocusedLabelColor = Color.Black,     // Label color when not focused
                    disabledLabelColor = OutlinedTextFieldTokens.DisabledLabelColor.value
                        .copy(alpha = OutlinedTextFieldTokens.DisabledLabelOpacity),
                    errorLabelColor = OutlinedTextFieldTokens.ErrorLabelColor.value,
                    focusedPlaceholderColor = OutlinedTextFieldTokens.InputPlaceholderColor.value,
                    unfocusedPlaceholderColor = OutlinedTextFieldTokens.InputPlaceholderColor.value,
                    disabledPlaceholderColor = OutlinedTextFieldTokens.DisabledInputColor.value
                        .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                    errorPlaceholderColor = OutlinedTextFieldTokens.InputPlaceholderColor.value,
                    focusedSupportingTextColor = OutlinedTextFieldTokens.FocusSupportingColor.value,
                    unfocusedSupportingTextColor = OutlinedTextFieldTokens.SupportingColor.value,
                    disabledSupportingTextColor = OutlinedTextFieldTokens.DisabledSupportingColor
                        .value.copy(alpha = OutlinedTextFieldTokens.DisabledSupportingOpacity),
                    errorSupportingTextColor = OutlinedTextFieldTokens.ErrorSupportingColor.value,
                    focusedPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value,
                    unfocusedPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value,
                    disabledPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value
                        .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                    errorPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value,
                    focusedSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value,
                    unfocusedSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value,
                    disabledSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value
                        .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                    errorSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = password,
                textStyle = AppTheme.typography.labelLarge,
                onValueChange = {password = it},
                placeholder = { Text(text = "Password", color = AppTheme.colorScheme.primary,style = AppTheme.typography.labelNormal) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AppTheme.colorScheme.primary) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = OutlinedTextFieldTokens.FocusInputColor.value,
                    unfocusedTextColor = OutlinedTextFieldTokens.InputColor.value,
                    disabledTextColor = OutlinedTextFieldTokens.DisabledInputColor.value
                        .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                    errorTextColor = OutlinedTextFieldTokens.ErrorInputColor.value,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    cursorColor = Color.Gray,             // Cursor color
                    errorCursorColor = OutlinedTextFieldTokens.ErrorFocusCaretColor.value,
                    selectionColors = LocalTextSelectionColors.current,
                    focusedBorderColor = AppTheme.colorScheme.primary,      // Border color when focused
                    unfocusedBorderColor = AppTheme.colorScheme.primary,    // Border color when not focused
                    disabledBorderColor = Color.LightGray,// Border color when disabled
                    errorBorderColor = Color.Red,         // Border color in error state
                    focusedLeadingIconColor = OutlinedTextFieldTokens.FocusLeadingIconColor.value,
                    unfocusedLeadingIconColor = OutlinedTextFieldTokens.LeadingIconColor.value,
                    disabledLeadingIconColor = OutlinedTextFieldTokens.DisabledLeadingIconColor.value
                        .copy(alpha = OutlinedTextFieldTokens.DisabledLeadingIconOpacity),
                    errorLeadingIconColor = OutlinedTextFieldTokens.ErrorLeadingIconColor.value,
                    focusedTrailingIconColor = OutlinedTextFieldTokens.FocusTrailingIconColor.value,
                    unfocusedTrailingIconColor = OutlinedTextFieldTokens.TrailingIconColor.value,
                    disabledTrailingIconColor = OutlinedTextFieldTokens.DisabledTrailingIconColor
                        .value.copy(alpha = OutlinedTextFieldTokens.DisabledTrailingIconOpacity),
                    errorTrailingIconColor = OutlinedTextFieldTokens.ErrorTrailingIconColor.value,
                    focusedLabelColor = AppTheme.colorScheme.primary,       // Label color when focused
                    unfocusedLabelColor = Color.Black,     // Label color when not focused
                    disabledLabelColor = OutlinedTextFieldTokens.DisabledLabelColor.value
                        .copy(alpha = OutlinedTextFieldTokens.DisabledLabelOpacity),
                    errorLabelColor = OutlinedTextFieldTokens.ErrorLabelColor.value,
                    focusedPlaceholderColor = OutlinedTextFieldTokens.InputPlaceholderColor.value,
                    unfocusedPlaceholderColor = OutlinedTextFieldTokens.InputPlaceholderColor.value,
                    disabledPlaceholderColor = OutlinedTextFieldTokens.DisabledInputColor.value
                        .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                    errorPlaceholderColor = OutlinedTextFieldTokens.InputPlaceholderColor.value,
                    focusedSupportingTextColor = OutlinedTextFieldTokens.FocusSupportingColor.value,
                    unfocusedSupportingTextColor = OutlinedTextFieldTokens.SupportingColor.value,
                    disabledSupportingTextColor = OutlinedTextFieldTokens.DisabledSupportingColor
                        .value.copy(alpha = OutlinedTextFieldTokens.DisabledSupportingOpacity),
                    errorSupportingTextColor = OutlinedTextFieldTokens.ErrorSupportingColor.value,
                    focusedPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value,
                    unfocusedPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value,
                    disabledPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value
                        .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                    errorPrefixColor = OutlinedTextFieldTokens.InputPrefixColor.value,
                    focusedSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value,
                    unfocusedSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value,
                    disabledSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value
                        .copy(alpha = OutlinedTextFieldTokens.DisabledInputOpacity),
                    errorSuffixColor = OutlinedTextFieldTokens.InputSuffixColor.value,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password
            Text(
                text = "Forgot Password ?",
                color = AppTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp),
                style = AppTheme.typography.labelNormal
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sign In Button
            Button(
                onClick = {
                    vm.signInUserWithEmailPass(email,password)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colorScheme.primary, contentColor = AppTheme.colorScheme.onPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Sign In",
                    color = AppTheme.colorScheme.onPrimary,
                    style = AppTheme.typography.labelNormal
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Option
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    style = AppTheme.typography.labelNormal,
                    color = AppTheme.colorScheme.primary
                )
                Text(
                    text = "Sign up",
                    color = AppTheme.colorScheme.primary,
                    style = AppTheme.typography.labelNormal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.SignUpScreen) // Adjust navigation logic
                    }
                )
            }
        }
    }
}
