package com.example.personalexpensetracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.personalexpensetracker.navigation.AppNavigation
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.d("FCM_TOKEN", "Token: $it")
        }

        setContent {
            AppNavigation()
        }
    }
}


