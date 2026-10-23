package com.example.lab10.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DeviceApiService {
    @GET("objects")
    suspend fun selectDevices(): ArrayList<DeviceModel>

    @GET("objects/{id}")
    suspend fun selectDevice(@Path("id") id: String): Response<DeviceModel>

    @Headers("Content-Type: application/json")
    @POST("objects")
    suspend fun insertDevice(@Body device: DeviceModel): Response<DeviceModel>

    @PUT("objects/{id}")
    suspend fun updateDevice(@Path("id") id: String, @Body device: DeviceModel): Response<DeviceModel>

    @DELETE("objects/{id}")
    suspend fun deleteDevice(@Path("id") id: String): Response<DeviceModel>
}