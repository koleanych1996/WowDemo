package com.example.wowdemo.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Product(
    @SerializedName("category")
    @Expose
    val category: Category,
    @SerializedName("colour")
    @Expose
    val colour: String,
    @SerializedName("details")
    @Expose
    val details: String,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("price")
    @Expose
    val price: Int,
    @SerializedName("size")
    @Expose
    val size: String,
    @SerializedName("sold_count")
    @Expose
    val soldCount: Int,

    var isFavourite: Boolean = false
)