package com.example.wowdemo.di

import com.example.wowdemo.WowOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@InstallIn(ViewModelComponent::class)
@Module
object NetworkModule {

    @Provides
    @WowOkHttpClient
    fun provideWowOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.connectTimeout(5, TimeUnit.MINUTES)
        clientBuilder.readTimeout(5, TimeUnit.MINUTES)
        clientBuilder.writeTimeout(5, TimeUnit.MINUTES)

        if (!clientBuilder.interceptors().contains(loggingInterceptor)) {
            clientBuilder.addInterceptor(loggingInterceptor)
        }

        return clientBuilder.build()
    }

}