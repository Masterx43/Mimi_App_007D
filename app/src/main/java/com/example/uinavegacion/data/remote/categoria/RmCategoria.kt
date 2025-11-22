package com.example.uinavegacion.data.remote.categoria

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RmCategoria {

    private const val BASE_URL = "https://rh35ncvl-8083.brs.devtunnels.ms/" // AJUSTAR
    //puerto: 8083
    //ipLH: 10.0.2.2
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

    val api: CategoriaServiceAPI by lazy {
        retrofit.create(CategoriaServiceAPI::class.java)
    }
}

