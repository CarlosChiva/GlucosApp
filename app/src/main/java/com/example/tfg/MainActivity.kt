package com.example.tfg

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tfg.controllers.SQLController
import com.example.tfg.models.SQLMaker
import com.example.tfg.databinding.ActivityMainBinding
import com.example.tfg.models.Datos
import java.time.LocalDateTime
import java.util.*

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
        //----------------------------------------------------------------------------------------- sql controller
        //        val sql = SQLMaker(this, "db", null, 1, null)
        //val qsl: SQLiteDatabase = sql.writableDatabase
        val currentDateTime = LocalDateTime.now()
        val controller = SQLController(this)
        val datos=Datos(currentDateTime, 80, 5, true, 100, false)


        controller.insertIntoMedida(datos)
        println("Lectura de datos")
        controller.loadDatesMedida()
//        val currentYear = currentDateTime.year
//        val currentMonth = currentDateTime.monthValue
//        val currentDay = currentDateTime.dayOfMonth
//        val currentHour = currentDateTime.hour
//        val currentMinute = currentDateTime.minute
//        val currentSecond = currentDateTime.second
//        val dateTimeString = "$currentYear-$currentMonth-$currentDay $currentHour:$currentMinute:$currentSecond"
//        Log.d("current date", dateTimeString)

//        qsl.execSQL("insert into medida(fecha) values('$dateTimeString');")


        //        qsl.close()
//        sql.close()
//        sql.close()

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}