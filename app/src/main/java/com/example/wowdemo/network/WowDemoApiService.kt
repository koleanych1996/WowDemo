package com.example.wowdemo.network

import com.example.wowdemo.model.GetProductsResponse
import retrofit2.http.GET

interface WowDemoApiService {

    @GET("products")
    suspend fun getProducts(): GetProductsResponse

}