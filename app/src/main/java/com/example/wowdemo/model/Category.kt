package com.example.wowdemo.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Category(
    @SerializedName("icon")
    @Expose
    val icon: String,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String
)