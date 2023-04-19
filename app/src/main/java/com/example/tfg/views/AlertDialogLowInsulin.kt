package com.example.tfg.views

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.TextView
import com.example.tfg.R
import com.example.tfg.models.ConfiguracionModel

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
        val label = view.findViewById<TextView>(R.id.labelLowInsulinValue)
        label.text = "Unidades Insulina Lenta"

        val value = view.findViewById<TextView>(R.id.lowInsulinValue)
        value.text = valueLoaded.toString()
        val seekbar = view.findViewById<SeekBar>(R.id.seekbarLowInsulin)
        seekbar.max = 80
        seekbar.min = 0
        seekbar.progress = valueLoaded!!.toInt()
        val builder = AlertDialog.Builder(context)

            .setPositiveButton(android.R.string.ok) { _, _ ->
                configuracionModel!!.lowInsulinSet((value.text.toString()).toInt())
         configuracionModel!!.saveVAlues()
            }

            .setTitle("Insulina Accion Lenta")
            .setView(view)

        builder.show()


        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                value.text = progress.toString()

                label.text =
                    if (value.text.equals(valueLoaded.toString())) "Unidades Insulina Lenta" else "Nuevo valor Unidades"


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//
            }
        })

        // items de la view    seekbar, despues saveValues() en configuration model

    }


}