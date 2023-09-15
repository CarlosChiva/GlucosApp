package com.example.tfg.controllers

import android.content.Context
import com.example.tfg.models.ConfiguracionModel
import com.example.tfg.models.Data
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PushPullDates(val context: Context) {
    val configuration = ConfiguracionModel(this.context)
    fun pushDates(): Map<String, Data> {
        val measures = SQLController(this.context).loadDatesMedida()
        measures.reverse()
        return measures.associateBy { it.date.toString() }
    }

    fun pushDatesForeign() :Map<String,Int>{
        val dates = SQLController(this.context).loadDatesForeign()
        val resultMap = mutableMapOf<String, Int>()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        for ((fecha, valor) in dates) {
            val fechaComoString = fecha.format(formatter)
            resultMap[fechaComoString] = valor
        }

        return resultMap

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
}