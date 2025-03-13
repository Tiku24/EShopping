package com.example.eshopping

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eshopping.presentation.navigation.NavApp
import com.example.eshopping.ui.theme.AppTheme
import com.google.firebase.auth.FirebaseAuth
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {
    @Inject lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp)
                ) {
                    NavApp(auth = auth)
                }
            }
        }
        Checkout.preload(applicationContext)
    }

     fun startPayment(
         amount: Int,
         name: String,
     ) {
         val checkout = Checkout()
         checkout.setKeyID("rzp_test_9dpoyTrg4wm8MV") // Replace with your Test Key

         try {
             val options = JSONObject().apply {
                 put("name", name)
                 put("description", "Test Payment")
                 put("currency", "INR")
                 put("amount", amount*100) // Amount in paise (50000 = ₹500)
                 put("prefill", JSONObject().apply {
                     put("email", "test@example.com")
                     put("contact", "9999999999")
                 })
             }
             checkout.open(this@MainActivity, options)
         } catch (e: Exception) {
             e.printStackTrace()
         }
    }
    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "Payment Successful $p0", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Error: $p1", Toast.LENGTH_SHORT).show()
        Log.d("TAGRoz", "onPaymentError: $p1")
    }
}
