package com.example.tfg.models

import android.content.Context
import com.example.tfg.controllers.SQLController
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
        for (i in 0..180) {
            val long = i.toLong()


            val datosMedida = createDatosMedida(fehca_actual.minusDays(long))
            for (dato in datosMedida) {
                sqlController.insertIntoMedida(dato)
            }
            val datosForeign = crearDatosCada5Minutos(fehca_actual.minusDays(long))
            sqlController.insertIntofOREIGNMedida(datosForeign)
        }

    }

    fun createDatosMedida(fechas: LocalDateTime): List<Datos> {
        val fecha = fechas.withHour(0).withMinute(0).withSecond(0).withNano(0)
        val objects = mutableListOf<Datos>()
        objects.add(Datos(fecha.withHour(6), 100, 20, true, false, 50, true))
        objects.add(Datos(fecha.withHour(12), 120, 30, false, true, 60, false))
        objects.add(Datos(fecha.withHour(18), 150, 40, true, true, 70, true))
        objects.add(Datos(fecha.withHour(0), 80, 10, false, false, 80, false))
        return objects
    }

    fun crearDatosCada5Minutos(fechas: LocalDateTime): List<Pair<LocalDateTime, Int>> {
        val fechaInicial = fechas.withHour(0).withMinute(0).withSecond(0).withNano(0)
        val fechaFinal = fechas.withHour(23).withMinute(59).withSecond(59).withNano(0)

        val horasAEvitar = listOf(
            fechaInicial.withHour(6),
            fechaInicial.withHour(12),
            fechaInicial.withHour(18),
            fechaInicial.withHour(0)
        )

        var fechaActual = fechaInicial
        var valorInt = 80
        var subiendo = true
        val datos = mutableListOf<Pair<LocalDateTime, Int>>()
        var valorMaximo = 180
        var valorMinimo = 80

        while (fechaActual <= fechaFinal) {
            if (!horasAEvitar.contains(fechaActual)) {
                datos.add(Pair(fechaActual, valorInt))
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