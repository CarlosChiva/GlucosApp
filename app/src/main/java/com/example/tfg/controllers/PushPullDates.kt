package com.example.tfg.controllers

import android.content.Context
import com.example.tfg.models.ConfiguracionModel

class PushPullDates(val firebaseController:FireBaseController){
    val context = firebaseController.context
    val SQLController = SQLController(this.context)
    val configuration = ConfiguracionModel(this.context)
    val fireBaseController = firebaseController
    fun pushDates(){

    }
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