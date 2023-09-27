package com.example.glucosApp.models

import android.content.Context
import com.example.glucosApp.controllers.SQLController
import java.time.LocalDateTime

class DemoDatosBase(context: Context) {
    val sqlController: SQLController
    var fehca_actual: LocalDateTime

    init {
        sqlController = SQLController(context)
        fehca_actual = LocalDateTime.now()
        initInsertDAta()
    }

    fun initInsertDAta() {
        //generacion de datos desde el dia mas lejano hasta un dia antes para demo e insercion en la bd
        for (i in 180 downTo 1) {
            val long = i.toLong()

            val datosMedida = createDatosMedida(fehca_actual.minusDays(long))
            for (dato in datosMedida) {
                sqlController.insertIntoMeasure(dato)
            }
            val datosForeign = crearDatosCada5Minutos(fehca_actual.minusDays(long))
            sqlController.insertIntofOREIGNMeasure(datosForeign)
        }
        sqlController.closeDataBase()
    }

    //datos de medida Demo
    fun createDatosMedida(fechas: LocalDateTime): List<Data> {
        val fecha = fechas.withHour(0).withMinute(0).withSecond(0).withNano(0)
        val objects = mutableListOf<Data>()
        objects.add(Data(fecha.withHour(6), 100, 10, true, false, 50, true))
        objects.add(Data(fecha.withHour(12), 120, 20, false, true, 60, false))
        objects.add(Data(fecha.withHour(18), 150, 5, true, true, 70, true))
        objects.add(Data(fecha.withHour(0), 80, 3, false, false, 80, false))
        objects.add(Data(fecha.withHour(2), 80, 30, false, false, 80, false))

        return objects
    }

    //datos para foreignMedida
    fun crearDatosCada5Minutos(fechas: LocalDateTime): List<Foreign> {
        val fechaInicial = fechas.withHour(0).withMinute(0).withSecond(0).withNano(0)
        val fechaFinal = fechas.withHour(23).withMinute(59).withSecond(59).withNano(0)

        val horasAEvitar = listOf(
            fechaInicial.withHour(6),
            fechaInicial.withHour(12),
            fechaInicial.withHour(18),
            fechaInicial.withHour(0),
            fechaInicial.withHour(2)
        )

        var fechaActual = fechaInicial
        var valorInt = 80
        var subiendo = true
        val datos = mutableListOf<Foreign>()
        var valorMaximo = 180
        var valorMinimo = 80
        while (fechaActual <= fechaFinal) {
            if (!horasAEvitar.contains(fechaActual)) {
                datos.add(Foreign(fechaActual, valorInt))
            }

            fechaActual = fechaActual.plusMinutes(5)
            if (fechaActual.monthValue % 2 != 0) {
                valorMaximo = 80
                valorMinimo = 40
            } else {
                valorMaximo = 250
                valorMinimo = 80
            }
            if (fechaActual.minute % 10 == 0) {
                if (subiendo) {
                    valorInt += 5
                    if (valorInt >= valorMaximo) {
                        subiendo = false
                    }
                } else {
                    valorInt -= 5
                    if (valorInt <= valorMinimo) {
                        subiendo = true
                    }
                }
            }

        }

        return datos
    }

}