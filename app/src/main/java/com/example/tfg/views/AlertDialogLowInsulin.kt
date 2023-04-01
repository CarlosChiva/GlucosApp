package com.example.tfg.views

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.example.tfg.R
import com.example.tfg.controllers.SQLController
import com.example.tfg.models.ConfiguracionModel
import com.example.tfg.models.Datos
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import java.time.LocalDateTime

class AlertDialogLowInsulin(context: Context) {
    //----------------------------------------------------------------------------------------- sql controller
    var configuracionModel: ConfiguracionModel? = null
    val context: Context
    var valueLoaded: Int?

    init {

        this.context = context
        configuracionModel = ConfiguracionModel(context)
        valueLoaded = configuracionModel?.lowInsulin
        createDialog()

    }


    private fun createDialog() {
        val inflater = LayoutInflater.from(context)
        //hacer la view antes de seguir este paso
        val view = inflater.inflate(R.layout.low_insulin_alert, null)
//cargar registro lowInsulin en variable
        var label = view.findViewById<TextView>(R.id.labelLowInsulinValue)
        label.text = "Unidades Insulina Lenta"

        val value = view.findViewById<TextView>(R.id.lowInsulinValue)
        value.text = valueLoaded.toString()
        var seekbar = view.findViewById<SeekBar>(R.id.seekbarLowInsulin)
        seekbar.max=80
        seekbar.min=0
        val builder = AlertDialog.Builder(context)

            .setPositiveButton(android.R.string.ok) { dialog, which ->

            }
            .setTitle("Insulina Accion Lenta")
            .setView(view)
        builder.show()


        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                value.text = progress.toString()
                if (value.text.equals(valueLoaded.toString())) {
                    label.text = "Unidades Insulina Lenta"
                } else {
                    label.text = "Nuevo valor Unidades"
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                textView.setText(value.toString())
//                previewValue.visibility = View.INVISIBLE
//                value = 0
            }
        })


//        value.text = configuracionModel.lowInsulin.toString()

        // items de la view    seekbar, despues saveValues() en configuration model

    }


}