package com.reyhan.collect.services

import com.reyhan.collect.model.ResponseCollection
import com.reyhan.collect.model.ResponseServices
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface CollectionServices {
    @POST("collect/add")
    fun addCollect(
        @Part("name") name: RequestBody,
        @Part("address") address: RequestBody,
        @Part("outstanding") outstanding: RequestBody,
        ): Call<ResponseServices>

    @GET("collect/all")
    fun getAllCollect(): Call<ResponseCollection>

    @POST("collect/delete")
    fun deleteCollect(@Path("id") id: String): Call<ResponseServices>

    @POST("collect/update")
    fun updateCollect(
        @Path("id") id: String,
        @Part("name") name: RequestBody,
        @Part("address") address: RequestBody,
        @Part("outstanding") outstanding: RequestBody
    ): Call<ResponseServices>

}