package com.example.siceneapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.siceneapp.data.remote.LoginRepository
import com.example.siceneapp.data.remote.SicenetApi
import com.example.siceneapp.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api = RetrofitClient.retrofit.create(SicenetApi::class.java)
        val repository = LoginRepository(api)

        setContent {
            var matricula by remember { mutableStateOf("") }
            var contrasenia by remember { mutableStateOf("") }
            var tipoUsuario by remember { mutableStateOf("ALUMNO") }
            var message by remember { mutableStateOf("") }

            Column(modifier = Modifier.padding(16.dp)) {

                OutlinedTextField(
                    value = matricula,
                    onValueChange = { matricula = it },
                    label = { Text("Matrícula") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contrasenia,
                    onValueChange = { contrasenia = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = tipoUsuario,
                    onValueChange = { tipoUsuario = it },
                    label = { Text("Tipo Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response: ResponseBody = repository.login(matricula, contrasenia, tipoUsuario)
                            val responseText = response.string()

                            runOnUiThread {
                                message = "Login exitoso: $responseText"
                                Toast.makeText(this@MainActivity, "Cookie guardada!", Toast.LENGTH_SHORT).show()
                                println("Cookie guardada: ${com.example.siceneapp.network.SessionManager.sessionCookie}")

                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                message = "Error: ${e.message}"
                            }
                        }
                    }
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(16.dp))
                // --- BOTÓN VER PERFIL ---
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response: ResponseBody = repository.getPerfil()
                            val responseText = response.string()

                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "Perfil obtenido!", Toast.LENGTH_SHORT).show()
                                println("Perfil XML: $responseText") // Ver XML en Logcat
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Ver Perfil")
                }

                Spacer(modifier = Modifier.height(16.dp)) // espacio antes del mensaje
                if (message.isNotEmpty()) {
                    Text(text = message)
                }
            }
        }
    }
}
