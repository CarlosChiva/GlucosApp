package com.example.tfg.controllers

import android.content.Context
import com.example.tfg.models.ConfiguracionModel

class PushPullDates(val context:Context){
    val sQLController = SQLController(this.context)
    val configuration = ConfiguracionModel(this.context)
    fun pushDates(){
    val measures=sQLController.loadDatesMedida()

    }
    fun pushDatesForeign(){
        val dates=sQLController.readLastDatesToForeign()

    }
     fun pullDatesMeasure(){
        //datos de configuracion(salvo usuario)
      /*  configuration.glucosaMaximaSet()
        configuration.glucosaMinimaSet()
        configuration.alarmaSet()
        configuration.lowInsulinSet()
        configuration.sensibilityFactorSet()
    */
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
    fun pullConfiguration(mutableList:List<Int>){
        configuration.configurationSet(mutableList)
    }
}