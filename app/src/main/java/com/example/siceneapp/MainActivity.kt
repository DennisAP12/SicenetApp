package com.example.siceneapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.siceneapp.data.remote.LoginRepository
import com.example.siceneapp.data.remote.SicenetApi
import com.example.siceneapp.data.remote.dto.StudentProfile
import com.example.siceneapp.network.RetrofitClient
import com.example.siceneapp.screens.LoginForm
import com.example.siceneapp.screens.ProfileScreen
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : ComponentActivity() {

    private val api by lazy { RetrofitClient.retrofit.create(SicenetApi::class.java) }
    private val repository by lazy { LoginRepository(api) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {

                    composable("login") {
                        LoginForm(
                            repository = repository,
                            onProfileSuccess = { profile ->
                                val profileJson = Json.encodeToString(StudentProfile.serializer(), profile)
                                // Codificamos el JSON para que sea seguro para la URL
                                val encodedProfileJson = URLEncoder.encode(profileJson, "UTF-8")
                                navController.navigate("profile/$encodedProfileJson")
                            }
                        )
                    }

                    composable("profile/{profileJson}") { backStackEntry ->
                        // Obtenemos el JSON codificado
                        val encodedProfileJson = backStackEntry.arguments?.getString("profileJson")
                        if (encodedProfileJson != null) {
                            // Decodificamos el JSON para restaurar el original
                            val profileJson = URLDecoder.decode(encodedProfileJson, "UTF-8")
                            val profile = Json.decodeFromString(StudentProfile.serializer(), profileJson)
                            ProfileScreen(profile = profile)
                        }
                    }
                }
            }
        }
    }
}
