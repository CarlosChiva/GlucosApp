package com.example.tfg.controllers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.models.ConfiguracionModel
import com.example.tfg.models.Datos

class StadisticAdapter(
    context: Context, val listItems: MutableList<Int>,
) :
    RecyclerView.Adapter<StadisticAdapter.ViewHolder>() {

    //Inner Class who inicializes the differents components of cards Views of what makes up the items
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val number=itemView.findViewById<TextView>(R.id.number)
    }
//inner Method who draw inner layout in specified screen

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.stadistic_item, parent, false)
        )
    }

    //Set values of differents items on cardViews denpendson item of the list and his individuals values
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.number.text=item.toString()
    }

    override fun getItemCount(): Int = listItems.size
}