package com.example.siceneapp.data.remote

import com.example.siceneapp.data.remote.dto.StudentProfile
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody

class LoginRepository(private val api: SicenetApi) {

    // Configuración del parser de JSON para que sea flexible
    private val json = Json { ignoreUnknownKeys = true }

    private fun buildLoginXml(matricula: String, contrasenia: String, tipoUsuario: String): RequestBody {
        val xml = """
            <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                           xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
                           xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <accesoLogin xmlns="http://tempuri.org/">
                  <strMatricula>$matricula</strMatricula>
                  <strContrasenia>$contrasenia</strContrasenia>
                  <tipoUsuario>$tipoUsuario</tipoUsuario>
                </accesoLogin>
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()

        return xml.toRequestBody("text/xml; charset=utf-8".toMediaType())
    }

    suspend fun login(matricula: String, contrasenia: String, tipoUsuario: String): ResponseBody {
        return api.login(buildLoginXml(matricula, contrasenia, tipoUsuario))
    }

    private fun buildProfileXml(): RequestBody {
        val xml = """
            <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                           xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                           xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <getAlumnoAcademicoWithLineamiento xmlns="http://tempuri.org/" />
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()
        return xml.toRequestBody("text/xml; charset=utf-8".toMediaType())
    }

    /**
     * Obtiene y parsea el perfil del alumno.
     * 1. Llama a la API. Retrofit usa SimpleXML para obtener un objeto ProfileResponse.
     * 2. Extrae el string JSON de la respuesta.
     * 3. Usa Kotlinx Serialization para convertir el JSON en un objeto StudentProfile.
     */
    suspend fun getProfile(): StudentProfile? {
        val response = api.getAlumnoAcademicoWithLineamiento(buildProfileXml())
        val jsonString = response.result ?: return null

        return try {
            json.decodeFromString<StudentProfile>(jsonString)
        } catch (e: Exception) {
            println("Error al parsear el JSON del perfil: ${e.message}")
            null
        }
    }
}
