package com.reyhan.collect.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConfig {
    fun getRetrofit(): Retrofit {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(HeaderInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://544d-182-2-165-151.ngrok-free.app/cicool/api/")

            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getServiceCollection() = getRetrofit().create(CollectionServices::class.java)
}