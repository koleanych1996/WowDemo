package com.example.wowdemo.network

import com.example.wowdemo.model.GetProductsResponse
import com.example.wowdemo.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface WowDemoApiService {

    @GET("products")
    suspend fun getProducts(): GetProductsResponse

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") productId: Int): Product

}