package com.example.siceneapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.siceneapp.data.remote.LoginRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.siceneapp.network.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LoginForm(repository: LoginRepository) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var matricula by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    var tipoUsuario by remember { mutableStateOf("ALUMNO") }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        // --- CAMPOS DE TEXTO ---
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

        // --- BOTÓN LOGIN ---
        Button(onClick = {
            scope.launch {
                try {
                    val responseText = withContext(Dispatchers.IO) {
                        val response: ResponseBody = repository.login(matricula, contrasenia, tipoUsuario)
                        response.string() // Esta operación de lectura es bloqueante, por eso se queda en el contexto de IO
                    }

                    // Ahora estamos de vuelta en el hilo principal automáticamente
                    message = "Login exitoso: $responseText"
                    Toast.makeText(context, "Cookie guardada!", Toast.LENGTH_SHORT).show()
                    println("Cookie guardada: ${SessionManager.sessionCookie}")

                } catch (e: Exception) {
                    message = "Error: ${e.message}"
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- BOTÓN VER PERFIL ---
        Button(onClick = {
            scope.launch {
                try {
                    val responseText = withContext(Dispatchers.IO) {
                        val response: ResponseBody = repository.getPerfil()
                        response.string()
                    }
                    Toast.makeText(context, "Perfil obtenido!", Toast.LENGTH_SHORT).show()
                    println("Perfil XML: $responseText")

                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Ver Perfil")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- MENSAJE ---
        if (message.isNotEmpty()) {
            Text(text = message)
        }
    }
}
