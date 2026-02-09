package com.example.siceneapp.data.remote

import com.example.siceneapp.data.remote.dto.ProfileResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SicenetApi {
    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: \"http://tempuri.org/accesoLogin\"" // <-- ENCABEZADO AÑADIDO
    )
    @POST("ws/wsalumnos.asmx")
    suspend fun login(
        @Body body: RequestBody
    ): ResponseBody

    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: \"http://tempuri.org/getAlumnoAcademicoWithLineamiento\""
    )
    @POST("ws/wsalumnos.asmx")
    suspend fun getAlumnoAcademicoWithLineamiento(@Body body: RequestBody): ProfileResponse
}
