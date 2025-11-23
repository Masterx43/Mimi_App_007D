package com.example.uinavegacion.data.remote.authservice


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RmAuth {

    private const val BASE_URL = "https://2k6dwp9l-8085.brs.devtunnels.ms/" // Puerto del AuthService
    //puerto: 8085

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create()  )
        .build()

    val api: AuthServiceAPI by lazy {
        retrofit.create(AuthServiceAPI::class.java)
    }
}
