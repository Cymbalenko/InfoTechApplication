package com.example.infotechapplication.model.api.responses

data class Main(
    val feelslike: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val temp_max: Double,
    val tempmin: Double
)