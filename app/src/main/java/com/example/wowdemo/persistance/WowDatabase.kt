package com.example.wowdemo.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wowdemo.model.Product

@Database(
    entities = [
        Product::class,
    ],
    version = 1,
    exportSchema = false
)

abstract class WowDatabase : RoomDatabase() {

    abstract fun wowDao(): WowDemoDao

    companion object {
        const val DATABASE_NAME: String = "wow_db"
    }
}