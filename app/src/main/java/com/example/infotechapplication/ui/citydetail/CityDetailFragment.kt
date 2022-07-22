package com.example.infotechapplication.ui.citydetail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.infotechapplication.R
import com.example.infotechapplication.base.BaseViewBindingFragment
import com.example.infotechapplication.databinding.FragmentCityDetailBinding
import com.example.infotechapplication.model.api.responses.ResponseWeather
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CityDetailFragment  :  BaseViewBindingFragment<FragmentCityDetailBinding>() {

    companion object {
        fun newInstance() = CityDetailFragment()
    }

    private  val viewModel:CityDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            arguments?.let { g->
                 val city = g.getString("city")
                 val lon =  g.getString("lon").toString().toDouble()
                 val lat =  g.getString("lat").toString().toDouble()
                Log.e("gps","1lon-"+g.getString("lon")+"    lat-"+g.getString("lat"))
                 viewModel.getCurrent(city.toString(),lat.toString(),lon.toString())
                 binding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS,
                    object : Style.OnStyleLoaded {
                        override fun onStyleLoaded(style: Style) {
                            addAnnotationToMap(lat,lon)
                        }
                    })
                Log.e("gps","2lon-"+lon+"    lat-"+lat)
                binding.mapView.getMapboxMap().setCamera(
                    CameraOptions.Builder().zoom(13.0).center(Point.fromLngLat(lon, lat)).build()
                )
             }

        lifecycleScope.launchWhenStarted {
            viewModel.responseWeather
                .onEach {
                    load(it)
                }
                .launchIn(lifecycleScope)
        }
    }
    private fun load(weather:ResponseWeather){
        weather.let {
            it.main.let { main->
                main?.let {
                    binding.tvCurrentTempValue.text=it.temp.toString()
                    binding.tvWetnessValue.text=it.humidity.toString()
                    binding.tvAirTempValue.text=it.tempmin.toString()+" - "+it.temp_max.toString()
                }

            }
            it.weather.let { weatherIt->
                 weatherIt?.firstOrNull().let {
                     binding.tvDescriptionValue.text=it?.description
                }
            }

            it.wind.let { wind->
                binding.tvWindSpeedValue.text=wind?.speed.toString()
            }
        }
    }

    override fun initViewBinding(view: View): FragmentCityDetailBinding {
        return FragmentCityDetailBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        super.onDestroyView()
    }
    private fun addAnnotationToMap(lat:Double,lng:Double) {
        bitmapFromDrawableRes(
            context = requireContext(),
            R.drawable.red_marker
        )?.let {
            val annotationApi = binding.mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(lng, lat))
                .withIconImage(it)
            pointAnnotationManager.create(pointAnnotationOptions)
        }
    }
    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

}