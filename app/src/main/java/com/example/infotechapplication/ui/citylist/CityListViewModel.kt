package com.example.infotechapplication.ui.citylist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infotechapplication.model.json.CityList
import com.example.infotechapplication.model.json.CityListItem
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class CityListViewModel  : ViewModel(){
    private var  _cities = MutableLiveData<List<CityListItem>>()
    private var  _citiesFilter = MutableLiveData<List<CityListItem>>()
    val cities: LiveData<List<CityListItem>> = _cities
    var citiesFilter: LiveData<List<CityListItem>> = _citiesFilter

    var isLoading:Boolean = false
    var take:Int = 30
    var max:Int = 0
    var limit:Int = 20
    var filterString:String =  ""
    suspend fun getConvert(jsonString:String):CityList? = withContext(Dispatchers.IO) {
        val mapper = jacksonObjectMapper()
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)

         mapper.readValue( jsonString ,CityList::class.java)
    }
   fun convert( jsonFile: CityList?) {
         _cities.value=jsonFile
       filterReady()
   }
    fun filter(key:Boolean=false ,up:Boolean=false,down:Boolean=false ){

        if(key){
            take = 30
        }

        if(filterString.length>0) {
            var arr =
                _cities.value?.filter { a -> a.name.uppercase().contains(filterString.uppercase()) }
            max=arr?.size!!
            if(down)
                down()
            _citiesFilter.value = arr.take(take)
        }else{
            var arr =
                _cities.value
            max=arr?.size!!
            if(down)
                down()
            _citiesFilter.value = arr.take(take)
        }
        max=_citiesFilter.value?.size!!

    }
    fun filterReady(){

        Log.e("jsonres", "filterReady" )
        take = 30
         _cities.value.let{
            _citiesFilter.value = it?.take(take)
         }
        Log.e("jsonres", "_citiesFilter"+_citiesFilter.value )

    }
    private fun down(){
        if(limit>=max) return
        take+=30
    }
}