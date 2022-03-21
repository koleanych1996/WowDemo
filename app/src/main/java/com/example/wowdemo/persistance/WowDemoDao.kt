package com.example.wowdemo.persistance

import androidx.room.*
import com.example.wowdemo.model.Product

@Dao
interface WowDemoDao {

    @Transaction
    @Query("SELECT * FROM products")
    suspend fun readAllProductsLocalStorage(): List<Product>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProducts(products: List<Product>)


    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun readProduct(productId: Int): Product?

}