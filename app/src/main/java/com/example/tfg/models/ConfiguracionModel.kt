package com.example.tfg.models

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.io.*

//Ya veremos
//Configuracion general de la aplicacion
class ConfiguracionModel(val context: Context) : java.io.Serializable {
    var alarma = 2
    var glucosaMaxima = 180
    var glucosaMinima = 90
    var sensitiveFactor = 0
    var ratioInsulin = 0
    var lowInsulin: Int = 26
    val FILE = "configValues.txt"

    init {

        loadValues()
    }

    private fun loadValues() {
        try {
            val fileIn: FileInputStream = context.openFileInput(FILE)
            val reader: InputStreamReader = InputStreamReader(fileIn)
            val br = BufferedReader(reader)
            var tec: String = br.readLine()
            val array = tec.split(" ")
            glucosaMaximaSet(array[0].toInt())
            glucosaMinimaSet(array[1].toInt())
            alarmaSet(array[2].toInt())
            lowInsulinSet(array[3].toInt())
            sensibilityFactorSet(array[4].toInt())
            ratioInsulinSet(array[5].toInt())
            fileIn.close()
            reader.close()
        } catch (ex: FileNotFoundException) {
            saveVAlues()
            loadValues()
        }
    }


    fun glucosaMaximaSet(integer: Int) {
        glucosaMaxima = integer
    }

    fun glucosaMinimaSet(integer: Int) {
        glucosaMinima = integer
    }

    fun alarmaSet(integer: Int) {
        alarma = integer
    }

    fun lowInsulinSet(integer: Int) {
        lowInsulin = integer
    }

    fun sensibilityFactorSet(integer: Int) {
        sensitiveFactor = integer
    }

    fun ratioInsulinSet(integer: Int) {
        ratioInsulin = integer
    }

    fun saveVAlues() {

        val texto =
            "$glucosaMaxima $glucosaMinima $alarma $lowInsulin $sensitiveFactor $ratioInsulin"
        val output: FileOutputStream = context.openFileOutput(FILE, AppCompatActivity.MODE_PRIVATE)
        output.write(texto.toByteArray())
        output.close()

    }

}