package com.example.tfg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tfg.controllers.SQLController
import com.example.tfg.databinding.ActivityMainBinding
import com.example.tfg.models.Datos
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        val sqlController = SQLController(this)
        var fehca_actual = LocalDateTime.now()
        for (i in 0..180) {
            val long=i.toLong()


            val datosMedida = createDatosMedida( fehca_actual.minusDays(long))
            for (dato in datosMedida) {
                sqlController.insertIntoMedida(dato)
            }
            val datosForeign = crearDatosCada5Minutos( fehca_actual.minusDays(long))
            sqlController.insertIntofOREIGNMedida(datosForeign)
        }
    }


    fun createDatosMedida(fechas: LocalDateTime): List<Datos> {
        val fecha = fechas.withHour(0).withMinute(0).withSecond(0).withNano(0)
        val objects = mutableListOf<Datos>()
        objects.add(Datos(fecha.withHour(9), 100, 20, true, false, 50, true))
        objects.add(Datos(fecha.withHour(11), 120, 30, false, true, 60, false))
        objects.add(Datos(fecha.withHour(14), 150, 40, true, true, 70, true))
        objects.add(Datos(fecha.withHour(16), 80, 10, false, false, 80, false))
        objects.add(Datos(fecha.withHour(21), 110, 25, true, true, 90, true))
        return objects
    }

    fun crearDatosCada5Minutos(fechas: LocalDateTime): List<Pair<LocalDateTime, Int>> {
        val fechaInicial = fechas.withHour(0).withMinute(0).withSecond(0).withNano(0)
        val fechaFinal = fechas.withHour(23).withMinute(59).withSecond(59).withNano(0)

        val horasAEvitar = listOf(
            fechaInicial.withHour(9),
            fechaInicial.withHour(11),
            fechaInicial.withHour(14),
            fechaInicial.withHour(16),
            fechaInicial.withHour(21),
            fechaInicial.withHour(23)
        )

        var fechaActual = fechaInicial
        var valorInt = 80
        var subiendo = true
        val datos = mutableListOf<Pair<LocalDateTime, Int>>()

        while (fechaActual <= fechaFinal) {
            if (!horasAEvitar.contains(fechaActual)) {
                datos.add(Pair(fechaActual, valorInt))
            }

            fechaActual = fechaActual.plusMinutes(5)

            if (fechaActual.minute % 10 == 0) {
                if (subiendo) {
                    valorInt += 5
                    if (valorInt >= 180) {
                        subiendo = false
                    }
                } else {
                    valorInt -= 5
                    if (valorInt <= 80) {
                        subiendo = true
                    }
                }
            }
        }

        return datos
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}