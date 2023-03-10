package com.example.tfg.controllers

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.models.ConfiguracionModel
import com.example.tfg.models.Datos


//Adapter for View Historical who needs a context of activity and MutableList of dates to draw him on the recycler
class HistoricalAdapter(
    private var context: Context, val listItems: MutableList<Datos>,
) :
    RecyclerView.Adapter<HistoricalAdapter.ViewHolder>() {
    val configurationModel = ConfiguracionModel(context)

    //Inner Class who inicializes the differents components of cards Views of what makes up the items
    class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        val glucosa: TextView = itemView.findViewById(R.id.glucValue)
        val date: TextView = itemView.findViewById(R.id.dateOfMedicion)
        val pick: ImageView = itemView.findViewById(R.id.pickIcon)
        val food: ImageView = itemView.findViewById(R.id.foodIcon)
        val alarm: ImageView = itemView.findViewById(R.id.alarmIcon)
       // val card: LinearLayout = itemView.findViewById(R.id.card)
        val insulin: TextView = itemView.findViewById(R.id.insulinTake)
        val chOfFoof: TextView = itemView.findViewById(R.id.chOfFood)
        val signalVAlue: ImageView = itemView.findViewById(R.id.signalCard)

    }
//inner Method who draw inner layout in specified screen

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.historical_item, parent, false),
            context
        )
    }

    //Set values of differents items on cardViews denpendson item of the list and his individuals values
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.glucosa.text = "${item.glucosa}\n mg/dl"
        holder.date.text = date(item)
        holder.signalVAlue.setBackgroundColor(backgroundView(item.glucosa))
        viewIcons(holder, item)
    }

    override fun getItemCount(): Int = listItems.size

    //Method for refactorized the date of the item on the list for put on the card view
    fun date(item: Datos): String {
        return "${item.fecha.dayOfMonth}/${item.fecha.month}/${item.fecha.year}  ${item.fecha.hour}:${item.fecha.minute}:${item.fecha.second}"

    }

    //Method who analized differents parametres of item of the list for draw his signal (if it have it) on the card view
    fun viewIcons(holder: ViewHolder, item: Datos) {
        if (item.alarma != null && item.alarma == true) {
            holder.alarm.setImageResource(R.drawable.ic_alarm)
        }
        if (item.pick != null) {
            holder.pick.setImageResource(R.drawable.ic_pick)
            holder.insulin.text = item.pick.toString()
        }
        if (item.food != null) {
            holder.food.setImageResource(R.drawable.ic_baseline_fastfood_24)
            if (item.CHfood != null) {
                holder.chOfFoof.text = "${item.CHfood} gr"
            }
        }
    }

    //Method who analized the number of level of gluc, paint the abckgroun denpend on level max and min maked to the configuration
//Falta añadir la configuracion
    fun backgroundView(glucosa: Int?): Int {
        when {
            glucosa!! >= configurationModel.glucosaMaxima ||glucosa < configurationModel.glucosaMinima-10 -> return Color.RED
            glucosa in configurationModel.glucosaMaxima - 20 until configurationModel.glucosaMaxima -> return Color.YELLOW
            glucosa in configurationModel.glucosaMinima-10..configurationModel.glucosaMinima -> return Color.YELLOW
            else -> return Color.GREEN
        }

    }
}