package com.example.tfg.controllers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import java.time.LocalDateTime


class StadisticAdapter(
    context: Context, val listItems: List<Pair<LocalDateTime, Int>>,
) :
    RecyclerView.Adapter<StadisticAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fecha = itemView.findViewById<TextView>(R.id.fecha)
        val number = itemView.findViewById<TextView>(R.id.number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.stadistic_item, parent, false)
        )
    }

      override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.fecha.text = item.first.toString()
        holder.number.text = item.second.toString()
    }

    override fun getItemCount(): Int = listItems.size
}