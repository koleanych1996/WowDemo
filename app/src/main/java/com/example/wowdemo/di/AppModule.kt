package com.example.wowdemo.di

import android.app.Application
import androidx.room.Room
import com.example.wowdemo.BuildConfig
import com.example.wowdemo.network.DateDeserializer
import com.example.wowdemo.persistance.WowDatabase
import com.example.wowdemo.persistance.WowDatabase.Companion.DATABASE_NAME
import com.example.wowdemo.persistance.WowDemoDao
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGsonInstance(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .create()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun provideAppDb(application: Application): WowDatabase {
        return Room.databaseBuilder(application, WowDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration() // get correct db version if schema changed
            .build()
    }

    @Singleton
    @Provides
    fun provideWowDao(
        wowDatabase: WowDatabase
    ): WowDemoDao {
        return wowDatabase.wowDao()
    }

}