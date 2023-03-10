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
        val currentDateTime = LocalDateTime.now()
        val controller = SQLController(this)
        val list= listOf(100,200,300,500,50,180,400,30)
        val datos=Datos(currentDateTime, list.get(list.size-1), 5, true, 100, false)
        controller.insertIntofOREIGNMedida(list.subList(0, list.size-2),currentDateTime)
       controller.insertIntoMedida(datos)


    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}