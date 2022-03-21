package com.example.wowdemo.network

import com.example.wowdemo.model.GetProductsResponse
import com.example.wowdemo.model.Product
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WowDemoApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 10
    ): GetProductsResponse

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") productId: Int): Product

}