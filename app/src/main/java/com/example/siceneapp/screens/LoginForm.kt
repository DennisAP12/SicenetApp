package com.example.siceneapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.siceneapp.data.remote.LoginRepository
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.siceneapp.data.remote.dto.StudentProfile

@Composable
fun LoginForm(
    repository: LoginRepository,
    onProfileSuccess: (StudentProfile) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var matricula by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    var tipoUsuario by remember { mutableStateOf("ALUMNO") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = matricula,
            onValueChange = { matricula = it },
            label = { Text("Matrícula") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = contrasenia,
            onValueChange = { contrasenia = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = tipoUsuario,
            onValueChange = { tipoUsuario = it },
            label = { Text("Tipo Usuario") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                scope.launch {
                    try {
                        // 1. Iniciar sesión y cerrar la respuesta para liberar recursos
                        repository.login(matricula, contrasenia, tipoUsuario).use { }

                        // 2. Con la cookie ahora visible gracias a @Volatile, obtener el perfil
                        val profile = repository.getProfile()

                        if (profile != null) {
                            onProfileSuccess(profile)
                        } else {
                            message = "Login exitoso, pero no se pudo obtener el perfil."
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        message = "Error: ${e.message}"
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Iniciar Sesión")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty() && !isLoading) {
            Text(text = message)
        }
    }
}
