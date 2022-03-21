package com.example.wowdemo.di

import com.example.wowdemo.network.WowDemoApiService
import com.example.wowdemo.repository.ProductDetailsRepository
import com.example.wowdemo.repository.ProductDetailsRepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ProductDetailsModule {

    @Provides
    fun provideProductDetailsRepository(
        gson: Gson,
        wowDemoApiService: WowDemoApiService
    ): ProductDetailsRepository {
        return ProductDetailsRepositoryImpl(
            gson,
            wowDemoApiService
        )
    }


}