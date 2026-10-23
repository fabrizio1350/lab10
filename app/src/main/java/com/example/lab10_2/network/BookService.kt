package com.example.lab10_2.network

import com.example.lab10_2.data.QueryResponse
import com.example.lab10_2.data.BookInfo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookService {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): QueryResponse

    @GET("volumes/{id}")
    suspend fun getBook(@Path("id") id: String): BookInfo
}