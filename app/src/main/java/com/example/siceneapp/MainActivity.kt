package com.example.siceneapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.siceneapp.data.remote.LoginRepository
import com.example.siceneapp.data.remote.SicenetApi
import com.example.siceneapp.network.RetrofitClient
import com.example.siceneapp.screens.LoginForm
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear API y Repository
        val api = RetrofitClient.retrofit.create(SicenetApi::class.java)
        val repository = LoginRepository(api)

        // SetContent con Compose
        setContent {
            MaterialTheme {
                // Mostramos el formulario de login
                LoginForm(repository = repository)
            }
        }
    }
}
