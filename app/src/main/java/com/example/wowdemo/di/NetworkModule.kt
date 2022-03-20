package com.example.wowdemo.di

import com.example.wowdemo.Constants
import com.example.wowdemo.WowOkHttpClient
import com.example.wowdemo.network.WowDemoApiService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@InstallIn(ViewModelComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideApiService(
        @WowOkHttpClient okHttpClient: OkHttpClient,
        gson: Gson
    ): WowDemoApiService {
        val retrofitBuilder = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.APP_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
        return retrofitBuilder.build().create(WowDemoApiService::class.java)
    }

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