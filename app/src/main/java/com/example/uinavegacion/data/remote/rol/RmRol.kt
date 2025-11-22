package com.example.uinavegacion.data.remote.rol


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RmRol {

    private const val BASE_URL = "https://rh35ncvl-8084.brs.devtunnels.ms/"
    //puerto: 8084

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: RolServiceAPI by lazy {
        retrofit.create(RolServiceAPI::class.java)
    }
}
