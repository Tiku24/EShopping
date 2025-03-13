package com.example.eshopping.presentation.screen

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.eshopping.R
import com.example.eshopping.common.IMAGES
import com.example.eshopping.presentation.navigation.Routes
import com.example.eshopping.presentation.viewmodel.MainViewModel
import com.example.eshopping.ui.theme.AppTheme
import com.example.eshopping.utils.uriToByteArray
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileScreenUI(
    auth: FirebaseAuth,
    vm:MainViewModel,
    navController: NavController
) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var imageUrl by remember { mutableStateOf("") }
    val pickMedia = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            imageUri = uri
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    val date = System.currentTimeMillis()
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
    LaunchedEffect(updateState.value.success) {
        vm.getUserById(auth.currentUser?.uid.toString())
    }

    LaunchedEffect(userState.value.success) {
        userState.value.success.let {
            firstName = it?.firstName.toString()
            lastName = it?.lastName.toString()
            email = it?.email.toString()
            address = it?.address.toString()
            phoneNumber = it?.phoneNumber.toString()
            imageUrl = it?.image.toString()
        }
    }

    when{
        updateState.value.success != null -> {
            Toast.makeText(context, updateState.value.success, Toast.LENGTH_SHORT).show()
            updateState.value.success = null
        }
    }

    when {
        userState.value.isLoading -> {
            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(),color = AppTheme.colorScheme.primary)
        }

        userState.value.error != null -> {
            Text(userState.value.error.toString())
        }

        userState.value.success != null -> {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppTheme.colorScheme.onPrimary)
            ) {
                // Top Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colorScheme.primary)
                        .padding(vertical = 30.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                        IconButton(onClick = {
                            navController.popBackStack()
                        },
                            colors = IconButtonColors(
                                contentColor = AppTheme.colorScheme.primary,
                                containerColor = AppTheme.colorScheme.onPrimary,
                                disabledContainerColor = AppTheme.colorScheme.primary,
                                disabledContentColor = AppTheme.colorScheme.primary)){ Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,contentDescription = null, tint = AppTheme.colorScheme.primary) }
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
                        if (imageUrl.isNotEmpty()){
                        AsyncImage(model = imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.size(120.dp).clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape)
                        )
                        }else{
                            Image(painter = painterResource(id = R.drawable.profile),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.size(120.dp).clip(CircleShape)
                                    .border(2.dp, Color.Gray, CircleShape))
                        }
                        IconButton(
                            onClick = {
                                pickMedia.launch("image/*")
                            },
                            enabled = !isEdit,
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
                            if (!isEdit) {
                                val imageByteArray = imageUri?.uriToByteArray(context)

                                if (imageByteArray != null) {
                                    // Step 1: Upload Image First
                                    vm.uploadImage("$date", imageByteArray)
                                        // Step 2: After Upload, Get the New Image URL
                                        vm.getImages(bucketName = IMAGES, fileName = "$date") { newImageUrl ->
                                            if (!newImageUrl.isNullOrEmpty()) {
                                                imageUrl = newImageUrl
                                                updatedFields["image"] = newImageUrl
                                                Log.d("TAGIMG", "New Image URL: $newImageUrl")

                                                // Step 3: Update User Profile with the New Image URL
                                                if (updatedFields.isNotEmpty()) {
                                                    vm.updateUser(updatedFields)
                                                }
                                            }
                                    }
                                } else {
                                    // If no new image, just update other profile fields
                                    if (updatedFields.isNotEmpty()) {
                                        vm.updateUser(updatedFields)
                                    }
                                }
                            }

                            isEdit = !isEdit
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colorScheme.primary, contentColor = AppTheme.colorScheme.onPrimary)
                    ) {
                        Text(if (isEdit) "Edit" else "Save changes",style = AppTheme.typography.labelNormal)
                    }

                    Button(onClick = {
                        vm.resetState()
                        auth.signOut()
                        navController.navigate(Routes.SignInScreen) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colorScheme.primary, contentColor = AppTheme.colorScheme.onPrimary)) {
                        Text(text = "Sign Out", style = AppTheme.typography.labelNormal)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(label: String, value: String, icon: ImageVector,onValueChange: (String) -> Unit,isEditable:Boolean) {
    OutlinedTextField(
        value = value,
        textStyle = AppTheme.typography.labelLarge,
        onValueChange = {onValueChange(it)},
        label = { Text(label,style = AppTheme.typography.labelNormal) },
        readOnly = isEditable,
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            cursorColor = Color.Gray,             // Cursor color
            focusedBorderColor = AppTheme.colorScheme.primary,      // Border color when focused
            unfocusedBorderColor = AppTheme.colorScheme.primary,    // Border color when not focused
            disabledBorderColor = Color.LightGray,// Border color when disabled
            errorBorderColor = Color.Red,         // Border color in error state
            focusedLabelColor = AppTheme.colorScheme.primary,       // Label color when focused
            unfocusedLabelColor = Color.Black,     // Label color when not focused
        )
    )
}