package com.example.wowdemo.model


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Entity(tableName = "products")
data class Product(

    @SerializedName("category")
    @Embedded(prefix = "category_")
    @Expose
    val category: Category,

    @SerializedName("colour")
    @ColumnInfo(name = "colour")
    @Expose
    val colour: String,

    @SerializedName("details")
    @ColumnInfo(name = "details")
    @Expose
    val details: String,

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    @Expose
    val name: String,

    @SerializedName("price")
    @ColumnInfo(name = "price")
    @Expose
    val price: Int,

    @SerializedName("size")
    @ColumnInfo(name = "size")
    @Expose
    val size: String,

    @SerializedName("sold_count")
    @ColumnInfo(name = "sold_count")
    @Expose
    val soldCount: Int,

    @ColumnInfo(name = "is_favourite")
    var isFavourite: Boolean = false,

    @ColumnInfo(name = "page")
    var page: Int? = null
)