package com.example.wowdemo.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Category(

    @SerializedName("icon")
    @ColumnInfo(name = "icon")
    @Expose
    val icon: String,

    @SerializedName("id")
    @ColumnInfo(name = "id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    @Expose
    val name: String
)