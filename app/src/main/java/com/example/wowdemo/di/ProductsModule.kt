package com.example.wowdemo.di

import android.content.Context
import com.example.wowdemo.network.WowDemoApiService
import com.example.wowdemo.persistance.WowDemoDao
import com.example.wowdemo.repository.ProductsRepository
import com.example.wowdemo.repository.ProductsRepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(ViewModelComponent::class)
object ProductsModule {

    @Provides
    fun provideProductsRepository(
        gson: Gson,
        wowDemoApiService: WowDemoApiService,
        wowDemoDao: WowDemoDao,
        @ApplicationContext context: Context
    ): ProductsRepository {
        return ProductsRepositoryImpl(
            gson,
            wowDemoApiService,
            wowDemoDao,
            context
        )
    }

}