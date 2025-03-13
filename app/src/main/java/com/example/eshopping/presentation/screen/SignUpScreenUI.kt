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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.eshopping.data.model.UserData
import com.example.eshopping.presentation.navigation.Routes
import com.example.eshopping.presentation.viewmodel.MainViewModel
import com.example.eshopping.ui.theme.AppTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenUI(modifier: Modifier = Modifier,vm: MainViewModel,navController: NavController) {
    val state by vm.registerUserWithEmailPassState.collectAsState()
    val context = LocalContext.current

    when{
        state.isLoading -> {
            CircularProgressIndicator(
                modifier
                    .fillMaxSize()
                    .wrapContentSize(),color = AppTheme.colorScheme.primary)
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
                    style = AppTheme.typography.titleNormal,
                    color = AppTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                OutlinedTextField(value = firstName,
                    textStyle = AppTheme.typography.labelLarge,
                    onValueChange = {firstName = it}, label = {Text("First Name",color = AppTheme.colorScheme.primary,style = AppTheme.typography.labelNormal)},colors = OutlinedTextFieldDefaults.colors(
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
                        .padding(horizontal = 16.dp))
                OutlinedTextField(value = lastName, onValueChange = {lastName = it},
                    textStyle = AppTheme.typography.labelLarge,
                    label = {Text("Last Name",color = AppTheme.colorScheme.primary,style = AppTheme.typography.labelNormal)},colors = OutlinedTextFieldDefaults.colors(
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
                        .padding(horizontal = 16.dp))
                OutlinedTextField(value = email, onValueChange = {email = it},
                    textStyle = AppTheme.typography.labelLarge,
                    label = {Text("Email",color = AppTheme.colorScheme.primary,style = AppTheme.typography.labelNormal)},colors = OutlinedTextFieldDefaults.colors(
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
                        .padding(horizontal = 16.dp))
                OutlinedTextField(value = password, onValueChange = {password = it},
                    textStyle = AppTheme.typography.labelLarge,
                    label = {Text("Password",color = AppTheme.colorScheme.primary,style = AppTheme.typography.labelNormal)},
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
                        .padding(horizontal = 16.dp))
                OutlinedTextField(value = address, onValueChange = {address = it},
                    textStyle = AppTheme.typography.labelLarge,
                    label = {Text("Address",color = AppTheme.colorScheme.primary,style = AppTheme.typography.labelNormal)},
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
                        .padding(horizontal = 16.dp))
                OutlinedTextField(value = phone, onValueChange = {phone = it},
                    textStyle = AppTheme.typography.labelLarge,
                    label = {Text("Phone Number",
                        color = AppTheme.colorScheme.primary,
                        style = AppTheme.typography.labelNormal
                    )
                    },colors = OutlinedTextFieldDefaults.colors(
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
                        .padding(horizontal = 16.dp))

                Button(onClick = {
                    vm.registerUserWithEmailPass(UserData(firstName = firstName, lastName = lastName, email = email, password = password, address = address, phoneNumber = phone, image = imageUrl))
                },
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colorScheme.primary, contentColor = AppTheme.colorScheme.onPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp))
                {
                    Text(text = "Sign Up",
                        color = AppTheme.colorScheme.onPrimary,
                        style = AppTheme.typography.labelNormal)
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Sign Up Option
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        style = AppTheme.typography.labelNormal,
                        color = AppTheme.colorScheme.primary
                    )
                    Text(
                        text = "Sign In",
                        color = AppTheme.colorScheme.primary,
                        style = AppTheme.typography.labelNormal,
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