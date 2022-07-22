package com.example.infotechapplication.model.api

import com.example.infotechapplication.model.api.responses.ResponseWeather
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("weather")
    suspend fun getForecast(
        @Query("q") id:String,
        @Query("lat") lat:String,
        @Query("lon") lon:String="yes",
        @Query("units") units:String="imperial",
        @Query("lang") lang:String="ua",
        @Query("mode") mode:String="json",
        @Header("X-RapidAPI-Key") key:String="fb0a66a2f5msh21c2715323e8bb2p145804jsn9e775bb60058",
        @Header("X-RapidAPI-Host") host:String="community-open-weather-map.p.rapidapi.com"
    ) : Response<ResponseWeather>


}