package com.example.infotechapplication.model.repository

import android.content.Context
import com.example.infotechapplication.model.api.ApiService
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService)  {
    suspend fun getForecast(city:String,lat:String,lon:String)=apiService.getForecast(city,lat ,lon )
}