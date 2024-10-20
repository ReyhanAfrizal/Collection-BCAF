package com.reyhan.collect.services

import com.reyhan.collect.model.ResponseCollection
import com.reyhan.collect.model.ResponseServices
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface CollectionServices {
    @Multipart
    @POST("collect/add")
    fun addCollect(
        @Part("name") name: RequestBody,
        @Part("address") address: RequestBody,
        @Part("outstanding") outstanding: RequestBody
    ): Call<ResponseServices>

    @GET("collect/all")
    fun getAllCollect(): Call<ResponseCollection>

    @Multipart
    @POST("collect/delete/") // Use DELETE for deleting
    fun deleteCollect(@Path("id") id: RequestBody): Call<ResponseServices>

    @Multipart
    @POST("collect/update")
    fun updateCollect(
        @Part("id") id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("address") address: RequestBody,
        @Part("outstanding") outstanding: RequestBody
    ): Call<ResponseServices>
}
