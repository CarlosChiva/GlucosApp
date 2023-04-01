package com.example.tfg.views

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.tfg.R
import com.example.tfg.controllers.SQLController
import com.example.tfg.models.ConfiguracionModel
import com.example.tfg.models.Datos
import com.google.android.material.imageview.ShapeableImageView
import java.time.LocalDateTime

class AlertDialogLowInsulin(context: Context) {
    //----------------------------------------------------------------------------------------- sql controller
    lateinit var configuracionModel: ConfiguracionModel
    val context: Context

    init {

        this.context = context
        createDialog()
        configuracionModel = ConfiguracionModel(context)

    }


    private fun createDialog() {

        val builder = AlertDialog.Builder(
            context
        )
        builder.setMessage("Insulina de Accion Lenta")
        val inflater = LayoutInflater.from(context)
        //hacer la view antes de seguir este paso
        //val view = inflater.inflate(R.layout.measure_alert, null)
//cargar registro lowInsulin en variable
//        val value = view.findViewById<TextView>(R.id.lowInsulineValue)
//        value.text = configuracionModel.lowInsulin.toString()

        // items de la view    seekbar, despues saveValues() en configuration model

//----------------------------------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------------------------


//        builder.setPositiveButton(android.R.string.ok)
//        { dialog, which ->
//            configuracionModel.lowInsulinSet(value)
//
//        }
//
//        builder.setTitle("Guardar")
//        builder.setView(view)
        builder.show()
    }



}