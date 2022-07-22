package com.example.infotechapplication.ui.citydetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infotechapplication.model.api.responses.ResponseWeather
import com.example.infotechapplication.model.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityDetailViewModel @Inject constructor(private val repository: ApiRepository):
    ViewModel() {
    private var  _responseWeather = MutableStateFlow<ResponseWeather>(ResponseWeather())
    val responseWeather: StateFlow<ResponseWeather> = _responseWeather.asStateFlow()
    fun getCurrent(city:String,lat:String,lon:String){
        Log.d("getCurrent","city-"+city+"/nlat-"+lat+"/nlon-"+lon)
        viewModelScope.launch {
            repository.getForecast(city,lat,lon).let { it ->
                if(it.isSuccessful){
                    Log.d("getCurrent","Succes: \n${it.body()}")
                    it.body()?.let { response->
                        _responseWeather.value=response
                    }
                }else{
                    Log.d("getCurrent","Error: ${it.errorBody()}")
                }
            }
        }
    }
}