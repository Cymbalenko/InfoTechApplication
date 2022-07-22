package com.example.infotechapplication.model.api.di

import com.example.infotechapplication.model.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun baseUrl()="https://community-open-weather-map.p.rapidapi.com/"

    @Provides
    @Singleton
    fun providerRetrofit(baseUrl:String ): ApiService = Retrofit.Builder()
        .baseUrl(baseUrl ) 
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create() )
        .build()
        .create(ApiService::class.java)
}