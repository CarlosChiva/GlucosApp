package com.example.tfg.controllers

import android.content.Context
import com.example.tfg.models.ConfiguracionModel

class PushPullDates(val context:Context){
    val sQLController = SQLController(this.context)
    val configuration = ConfiguracionModel(this.context)
    fun pushDates(){
    /*val measures=sQLController.loadDatesMedida()
    val dates=sQLController.
    */}

    fun pullDates(){
        //datos de configuracion(salvo usuario)
      /*  configuration.glucosaMaximaSet()
        configuration.glucosaMinimaSet()
        configuration.alarmaSet()
        configuration.lowInsulinSet()
        configuration.sensibilityFactorSet()
    */
    }
}