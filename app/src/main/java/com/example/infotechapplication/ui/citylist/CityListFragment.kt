package com.example.infotechapplication.ui.citylist


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.infotechapplication.R
import com.example.infotechapplication.base.BaseViewBindingFragment
import com.example.infotechapplication.databinding.FragmentCityListBinding
import com.example.infotechapplication.ui.citydetail.CityDetailFragment
import com.example.infotechapplication.ui.citylist.adapter.CityListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream


@AndroidEntryPoint
class CityListFragment  :  BaseViewBindingFragment<FragmentCityListBinding>() {


    companion object {
        fun newInstance() = CityListFragment()
    }

    private  val viewModel:CityListViewModel by viewModels()
    private val adapter = CityListAdapter { city, i ->
        when(i){
            1 -> {
                city.let {
                    createCityDetail(it.name,it.coord.lon,it.coord.lat)
                }
                Toast.makeText(requireContext(), city.name, Toast.LENGTH_LONG)
                    .show()
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView(view)
        binding.recyclerViewCity.clearOnScrollListeners()
        binding.recyclerViewCity.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!viewModel.isLoading) {
                    if (linearLayoutManager != null )
                        viewModel.isLoading = true
                        if(linearLayoutManager?.findLastCompletelyVisibleItemPosition() ==
                        (recyclerView.layoutManager?.itemCount)?.minus(1)
                            ) {
                        viewModel.filter(down = true)
                            Log.e("scroll","down")
                        }else if(linearLayoutManager?.findFirstCompletelyVisibleItemPosition() == 0
                                ) {
                            Log.e("scroll","up")
                        }
                    viewModel.isLoading = false

                }
            }
        })
        viewModel.citiesFilter.observe(viewLifecycleOwner) {
            Log.e("jsonres", "citiesFilter.observe"+it)

                adapter.submitList(it)
            }
            setupSearchView()
            lifecycleScope.launchWhenCreated {
            read()
        }
    }
    override fun initViewBinding(view: View): FragmentCityListBinding {
        return FragmentCityListBinding.bind(view)
    }
    private fun setUpRecyclerView(view: View) {
        val recyclerView = binding.recyclerViewCity
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
    private fun read(){
        try {
            if(viewModel.cities.value?.firstOrNull() != null) return
            lifecycleScope.launch (Dispatchers.Main) {
                val res = async { getJson() }
                val resItems = async { viewModel.getConvert(res.await()) }
                viewModel.convert(resItems.await())
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private suspend fun getJson( ): String
    = withContext(Dispatchers.Main){
        var str:String=""
        context?.let {
            val inputStream: InputStream = it.assets.open("city_list.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            str = String(buffer)
        }
        str
    }
    private fun setupSearchView() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let {
                    viewModel.filterString=it
                    viewModel.filter(true)
                }
                if(p0==null || p0.length==0){
                    viewModel.filterReady()
                    viewModel.filterString= ""
                }
                return false
            }
        })
    }
    private fun createCityDetail(city:String,lon:Double,lat:Double){
        try {
            val bundle = Bundle()
            bundle.putString("city", city)
            bundle.putString("lon", lon.toString())
            bundle.putString("lat", lat.toString())
            val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
            val fragment = CityDetailFragment.newInstance()
            fragment.arguments=bundle
            ft.replace( R.id.container, fragment, "fragment")
            ft.addToBackStack(null)
            ft.setCustomAnimations(
                android.R.animator.fade_in, android.R.animator.fade_out
            )
            ft.commit()
        }catch (e: Exception){
            Toast.makeText(this.context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}