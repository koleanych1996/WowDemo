package com.example.wowdemo.di

import com.example.wowdemo.Constants
import com.example.wowdemo.WowOkHttpClient
import com.example.wowdemo.network.WowDemoApiService
import com.example.wowdemo.repository.ProductsRepository
import com.example.wowdemo.repository.ProductsRepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(ViewModelComponent::class)
object ProductsModule {

    @Provides
    fun provideProductsRepository(
        gson: Gson,
        wowDemoApiService: WowDemoApiService
    ): ProductsRepository {
        return ProductsRepositoryImpl(
            gson,
            wowDemoApiService
        )
    }

}