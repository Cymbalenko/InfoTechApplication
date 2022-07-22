package com.example.infotechapplication.model.api.responses

data class ResponseWeather(
    val base: String,
    val clouds: Clouds?,
    val cod: Int,
    val coord: Coord?,
    val dt: Int,
    val id: Int,
    val main: Main?,
    val name: String,
    val sys: Sys?,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>?,
    val wind: Wind?
){
    constructor() : this("",null,0,null,0,0,null,"",null,0,0,null,null) {

    }
}