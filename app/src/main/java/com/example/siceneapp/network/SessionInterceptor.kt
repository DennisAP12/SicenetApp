package com.example.siceneapp.network

import okhttp3.Interceptor
import okhttp3.Response

class SessionInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        // 1️⃣ Tomamos la petición original
        val requestBuilder = chain.request().newBuilder()

        // 2️⃣ SI ya hay una cookie guardada, la enviamos
        SessionManager.sessionCookie?.let { cookie ->
            requestBuilder.addHeader("Cookie", cookie)
        }

        // 3️⃣ Enviamos la petición al servidor
        val response = chain.proceed(requestBuilder.build())

        // 4️⃣ Si el servidor manda una cookie, la guardamos
        response.headers("Set-Cookie").firstOrNull()?.let { cookie ->
            SessionManager.sessionCookie = cookie
        }

        // 5️⃣ Devolvemos la respuesta
        return response
    }
}
