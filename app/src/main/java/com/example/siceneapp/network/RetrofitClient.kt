package com.example.siceneapp.network
import okhttp3.OkHttpClient
import retrofit2.Retrofit
object RetrofitClient {
    private val client = OkHttpClient.Builder()
        .addInterceptor(SessionInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://sicenet.itsur.edu.mx/")
        .client(client)
        .build()
}