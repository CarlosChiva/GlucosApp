package com.example.tfg.controllers

import android.content.Context
import com.example.tfg.models.ConfiguracionModel

class Analizer(val context: Context) {
    val avgGlucMorning: Int
    val ratioInsul: Int
    val insulLenRecord: Int
    var sensibilityFactor: Int
    private val configurationModel= ConfiguracionModel(this.context)
    val insulLenRecom: Int

    init {
        avgGlucMorning = loadGlucMorning()
        sensibilityFactor = calcSensibilityFactor(totalFastQuery())
        ratioInsul = analizeInsulCH(loadArrayGLuc_CH())
        insulLenRecord = configurationModel.lowInsulin
        insulLenRecom = insulLenRecom()
        updateDates()
    }

    private fun analizeInsulCH(data: List<Array<Int>>): Int {
        // Separate the data into three lists
        val insulinList = data.map { it[0] }
        val carbList = data.map { it[1] }
        val glucose2hList = data.map { it[3] }
        // Calculate the average glucose difference (before and after meal)
        val avgGlucoseDiff = glucose2hList.zip(data.map { it[2] })
            .map { it.first - it.second }
            .average()
        // Calculate the average insulin to carb ratio
        val insulinToCarbRatio = insulinList.zip(carbList)
            .map { it.first.toDouble() / it.second }
            .average()
        // Calculate the insulin ratio
        return (insulinToCarbRatio * sensibilityFactor).toInt()
    }

    fun calcSensibilityFactor(dosisFastDiariaTotal: Int): Int {
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

    fun totalFastQuery(): Int {
        val sQLController = SQLController(this.context)
        return sQLController.totalFastInsulin()
    }

    fun insulLenRecom(): Int {
        return insulLenRecord + ((avgGlucMorning - 100) / sensibilityFactor)
    }


    private fun updateDates() {
        configurationModel.lowInsulinSet(insulLenRecord)
        configurationModel.ratioInsulinSet(ratioInsul)
        configurationModel.sensibilityFactorSet(sensibilityFactor)
        configurationModel.saveVAlues()

    }
    //-----------------------------------falta añadir
    fun insulFastRecom(racion: Int): Int {
        return racion * ratioInsul
    }

    fun correctGlucosa(currentGlucValue: Int): Int {
        return (currentGlucValue - 100) / sensibilityFactor
    }
}