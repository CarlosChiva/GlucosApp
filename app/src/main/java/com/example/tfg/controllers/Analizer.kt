package com.example.tfg.controllers

import android.content.Context
import com.example.tfg.models.ConfiguracionModel

class Analizer(val context: Context) {
    val avgGlucMorning: Int
    val ratioInsul: Float
    val insulLenRecord: Int
    var sensibilityFactor: Int
    private val configurationModel = ConfiguracionModel(this.context)
    val insulLenRecom: Int

    init {
        avgGlucMorning = loadGlucMorning()
        insulLenRecord = configurationModel.lowInsulinGet()
        sensibilityFactor = calcSensibilityFactor(totalFastQuery())
        ratioInsul = analizeInsulCH(loadArrayGLuc_CH())
        insulLenRecom = insulLenRecom()
        updateDates()
    }

    private fun analizeInsulCH(data: List<Array<Int>>): Float {
        // Separate the data into three lists
        val insulinList = data.map { it[0] }
        val carbList = data.map { it[1] }
        // Calculate the average insulin to carb ratio
        val insulinToCarbRatio = insulinList.zip(carbList)
            .map { it.first.toDouble() / it.second }
            .average()
        // Calculate the insulin ratio
        return (insulinToCarbRatio * sensibilityFactor).toFloat()
    }

    private fun calcSensibilityFactor(dosisFastDiariaTotal: Int): Int {
        val factorSensibilidad = 1700 / (dosisFastDiariaTotal + insulLenRecord)
        return factorSensibilidad
    }

    private fun loadArrayGLuc_CH(): List<Array<Int>> {
        val sqlController = SQLController(this.context)
        return sqlController.getInsuln_CH()

    }

    private fun loadGlucMorning(): Int {
        val sqlController = SQLController(this.context)
        return sqlController.readAvgMorning()
    }

    private fun totalFastQuery(): Int {
        val sQLController = SQLController(this.context)
        return sQLController.totalFastInsulin()
    }

    private fun insulLenRecom(): Int {
        return insulLenRecord + ((avgGlucMorning - 100) / sensibilityFactor)
    }


    private fun updateDates() {
        configurationModel.lowInsulinSet(insulLenRecord)
        configurationModel.ratioInsulinSet(ratioInsul)
        configurationModel.sensibilityFactorSet(sensibilityFactor)
        configurationModel.saveVAlues()

    }

    //-----------------------------------falta añadir
    fun insulFastRecom(racion: Int): Float {
        return racion * ratioInsul
    }

    fun correctGlucosa(currentGlucValue: Int): Int {
        return (currentGlucValue - 100) / sensibilityFactor
    }
}