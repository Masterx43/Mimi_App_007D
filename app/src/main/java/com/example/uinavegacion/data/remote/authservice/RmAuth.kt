package com.example.uinavegacion.data.remote.authservice

import com.example.uinavegacion.data.remote.authservice.AuthServiceAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RmAuth {

    private const val BASE_URL = "http://10.0.2.2:8085/" // Puerto del AuthService

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: AuthServiceAPI by lazy {
        retrofit.create(AuthServiceAPI::class.java)
    }
}
