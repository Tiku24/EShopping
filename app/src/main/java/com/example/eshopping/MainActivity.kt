package com.example.eshopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eshopping.presentation.navigation.NavApp
import com.example.eshopping.presentation.screen.HomeScreenUI
import com.example.eshopping.presentation.screen.SignInScreenUI
import com.example.eshopping.presentation.screen.SignUpScreenUI
import com.example.eshopping.ui.theme.EShoppingTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EShoppingTheme {
                Box(modifier = Modifier.fillMaxSize().padding(top = 50.dp)) {
                    NavApp(auth = auth)
                }
            }
        }
    }
}

