package com.example.wowdemo.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetProductsResponse(
    @SerializedName("count")
    @Expose
    val count: Int,
    @SerializedName("current_page")
    @Expose
    val currentPage: Int,
    @SerializedName("per_page")
    @Expose
    val perPage: Int,
    @SerializedName("results")
    @Expose
    val products: List<Product>,
    @SerializedName("total_pages")
    @Expose
    val totalPages: Int
)