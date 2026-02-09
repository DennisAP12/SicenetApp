package com.example.siceneapp.data.remote

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody

class LoginRepository(private val api: SicenetApi) {

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

    suspend fun getPerfil(): ResponseBody {
        return api.getAlumnoAcademicoWithLineamiento(buildProfileXml())
    }
}