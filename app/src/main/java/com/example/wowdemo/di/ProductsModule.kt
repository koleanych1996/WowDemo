package com.example.wowdemo.di

import com.example.wowdemo.repository.ProductsRepository
import com.example.wowdemo.repository.ProductsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object ProductsModule {

    @Provides
    fun provideDatabaseRepository(): ProductsRepository {
        return ProductsRepositoryImpl()
    }

}