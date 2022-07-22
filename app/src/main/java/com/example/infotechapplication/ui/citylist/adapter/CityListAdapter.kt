package com.example.infotechapplication.ui.citylist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.infotechapplication.R
import com.example.infotechapplication.model.json.CityListItem
import com.squareup.picasso.Picasso

class CityListAdapter (private val clickListener: (CityListItem,Int) -> Unit) : ListAdapter<CityListItem, CityListAdapter.CityListViewHolder>(CityListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_city_list_item, parent, false)
        return CityListViewHolder(view,clickListener)
    }

    override fun onBindViewHolder(holder: CityListViewHolder, position: Int) {
        getItem(position)?.let { city ->
            holder.bind(city,position)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
    inner class CityListViewHolder(private val view: View, private val clickListener: (CityListItem, Int) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(cities: CityListItem, position:Int) {
            val textView = itemView.findViewById<TextView>(R.id.tv_title)
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            if(position%2==0){
                Picasso.get().load("https://infotech.gov.ua/storage/img/Temp3.png").into(imageView)
            }else{
                Picasso.get().load("https://infotech.gov.ua/storage/img/Temp1.png").into(imageView)
            }
             textView.text=cities.name
            view.setOnClickListener{
                clickListener(cities,1)
            }
        }

    }

    class CityListDiffUtil : DiffUtil.ItemCallback<CityListItem>() {
        override fun areItemsTheSame(oldItem: CityListItem, newItem: CityListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CityListItem, newItem: CityListItem): Boolean {
            return oldItem == newItem
        }
    }
}