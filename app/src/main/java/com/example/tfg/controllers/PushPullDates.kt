package com.example.tfg.controllers

import android.content.Context
import android.util.Log
import com.example.tfg.models.ConfiguracionModel
import com.example.tfg.models.Data
import com.example.tfg.models.Foreign
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PushPullDates(val context: Context) {
    val configuration = ConfiguracionModel(this.context)
    fun pushDates(): Map<String, Data> {
        val measures = SQLController(this.context).loadDatesMedida()
        measures.reverse()
        return measures.associateBy { it.date.toString() }
    }

    fun pushDatesForeign(): Map<String, MutableList<Foreign>> {
        val dates = SQLController(this.context).loadDatesForeign()
        // Crea un mapa para almacenar las listas divididas por mes
        val groupedMap = mutableMapOf<String, MutableList<Foreign>>()

        for (foreign in dates) {
            val month = "${foreign.date.monthValue.toString()}-${foreign.date.year.toString()}"
            // Verifica si ya existe una lista para ese mes, si no, crea una nueva lista
            val foreignList = groupedMap.getOrPut(month) { mutableListOf() }

            // Agrega el objeto Foreign a la lista correspondiente
            foreignList.add(foreign)
        }

        return groupedMap
    }

    fun pullDatesMeasure(mutableList: MutableList<Data>) {
        mutableList.forEach {
            SQLController(this.context).insertIntoMeasure(it)
        }
    }

    fun pushConfiguration(): HashMap<String, Int> {
        val list = configuration.configurationGet()
        return hashMapOf(
            "glucoseMax" to list[0],
            "glucoseMin" to list[1],
            "alarm" to list[2],
            "lowInsulin" to list[3],
            "sensitiveFactor" to list[4],
            "ratioInsulin" to list[5]
        )
    }

    fun pullConfiguration(mutableList: List<Int>) {
        configuration.configurationSet(mutableList)
    }

    fun pullForeign(mutableList: List<Foreign>) {
        SQLController(this.context).insertForeignPull(mutableList)

    }
}