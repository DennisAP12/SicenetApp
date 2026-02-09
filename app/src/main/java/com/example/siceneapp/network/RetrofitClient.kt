package com.example.siceneapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object RetrofitClient {

    private val client = OkHttpClient.Builder()
        .addInterceptor(SessionInterceptor())
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://sicenet.itsur.edu.mx/")
        .client(client)
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()
}