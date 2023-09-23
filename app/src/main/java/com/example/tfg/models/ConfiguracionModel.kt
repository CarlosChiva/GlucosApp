package com.example.tfg.models

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.io.*


class ConfiguracionModel(val context: Context) : java.io.Serializable {
    private var alarm = 2
    private var glucoseMax = 180
    private var glucoseMin = 90
    private var sensitiveFactor = 0
    private var ratioInsulin = 0
    private var lowInsulin: Int = 26
    private var user = ""
    private var password = ""
    private val FILE = "configValues.txt"

    init {

        loadValues()
    }

    private fun loadValues() {
        try {
            val fileIn: FileInputStream = context.openFileInput(FILE)
            val reader: InputStreamReader = InputStreamReader(fileIn)
            val br = BufferedReader(reader)
            val tec: String = br.readLine()
            val array = tec.split(" ")
            glucosaMaximaSet(array[0].toInt())
            glucosaMinimaSet(array[1].toInt())
            alarmaSet(array[2].toInt())
            lowInsulinSet(array[3].toInt())
            sensibilityFactorSet(array[4].toInt())
            ratioInsulinSet(array[5].toInt())
            userSet(array[6])
            passwordSet(array[7])
            fileIn.close()
            reader.close()
        } catch (ex: FileNotFoundException) {
            saveVAlues()
            loadValues()
        }
    }


    fun glucosaMaximaSet(integer: Int) {
        glucoseMax = integer
    }

    fun glucosaMaximaGet(): Int {
        return glucoseMax
    }

    fun glucosaMinimaSet(integer: Int) {
        glucoseMin = integer
    }

    fun glucosaMinimaGet(): Int {
        return glucoseMin
    }

    fun alarmaSet(integer: Int) {
        alarm = integer
    }

    fun alarmaGet(): Int {
        return alarm
    }

    fun lowInsulinSet(integer: Int) {
        lowInsulin = integer
    }

    fun lowInsulinGet(): Int {
        return lowInsulin
    }

    fun sensibilityFactorSet(integer: Int) {
        sensitiveFactor = integer
    }

    fun sensibilityFactorGet(): Int {
        return sensitiveFactor
    }

    fun ratioInsulinSet(integer: Int) {
        ratioInsulin = integer
    }

    fun ratioInsulinGet(): Int {
        return ratioInsulin
    }

    fun userSet(string: String) {
        user = string
    }

    fun userGet(): String {
        return user
    }

    fun passwordGet(): String {
        return password
    }

    fun passwordSet(string: String) {
        password = string
    }

    fun configurationGet(): MutableList<Any> {
        return mutableListOf<Any>(
            glucoseMax,
            glucoseMin,
            alarm,
            lowInsulin,
            sensitiveFactor,
            ratioInsulin,
            user,
            password
        )

    }

    fun configurationSet(mutableList: List<Any>) {
        glucosaMaximaSet(mutableList[0].toString().toInt())
        glucosaMinimaSet(mutableList[1].toString().toInt())
        alarmaSet(mutableList[2].toString().toInt())
        lowInsulinSet(mutableList[3].toString().toInt())
        sensibilityFactorSet(mutableList[4].toString().toInt())
        ratioInsulinSet(mutableList[5].toString().toInt())
        saveVAlues()

    }

    fun saveVAlues() {
        val text =
            "$glucoseMax $glucoseMin $alarm $lowInsulin $sensitiveFactor $ratioInsulin $user $password"
        val output: FileOutputStream = context.openFileOutput(FILE, AppCompatActivity.MODE_PRIVATE)
        output.write(text.toByteArray())
        output.close()

    }

}