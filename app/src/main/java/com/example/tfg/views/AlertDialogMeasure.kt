package com.example.tfg.views

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.*
import com.example.tfg.R
import com.example.tfg.controllers.SQLController
import com.example.tfg.models.ConfiguracionModel
import com.example.tfg.models.Data
import com.google.android.material.imageview.ShapeableImageView
import java.time.LocalDateTime

class AlertDialogMeasure(context: Context, value: Int, currentDateTime: LocalDateTime) {
    //----------------------------------------------------------------------------------------- sql controller
    var controller: SQLController
    val value: Int
    val context: Context
    var hasMapBooleans: HashMap<String, Boolean> = hashMapOf()
    val ALARM = "alarm"
    val PICK = "pick"
    val FOOD = "food"
    val currentDateTime: LocalDateTime

    init {
        this.currentDateTime=currentDateTime
        this.controller = SQLController(context)
        this.value = value
        this.context = context
        createDialog()
        hasMapBooleans[ALARM] = false
        hasMapBooleans[PICK] = false
        hasMapBooleans[FOOD] = false
    }


    private fun createDialog() {

        val builder = AlertDialog.Builder(
            context
        )
        builder.setMessage("Algo que configurar?")
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.measure_alert, null)

        val gluc = view.findViewById<TextView>(R.id.glucValue)
        gluc.text = value.toString()

        val date = view.findViewById<TextView>(R.id.dateOfMedicion)
        date.text =
            "${currentDateTime.dayOfMonth}-${currentDateTime.month.value}-${currentDateTime.year} ${currentDateTime.hour}:${currentDateTime.minute}:${currentDateTime.second} "


        val pick = view.findViewById<ImageView>(R.id.pickIcon)
        pick.setImageResource(R.drawable.ic_pick)
        pick.setOnClickListener {
            selectedIcon(pick, PICK)
        }
        val alarm = view.findViewById<ImageView>(R.id.alarmIcon)
        alarm.setImageResource(R.drawable.ic_alarm)
        alarm.setOnClickListener {
            selectedIcon(alarm, ALARM)
        }
        val foodIcon = view.findViewById<ImageView>(R.id.foodIcon)
        foodIcon.setImageResource(R.drawable.ic_baseline_fastfood_24)
        foodIcon.setOnClickListener {
            selectedIcon(foodIcon, FOOD)
        }
        val signalVAlue = view.findViewById<ShapeableImageView>(R.id.signalCard)
        signalVAlue.setBackgroundColor(backgroundView(value))


//----------------------------------------------------------------------------------------------------------

        val insulinTake = view.findViewById<EditText>(R.id.insulinTake)
        val chfood = view.findViewById<EditText>(R.id.chOfFood)

        //--------------------------------------------------------------------------------------------------


        builder.setPositiveButton(android.R.string.ok)
        { dialog, which ->
            val datos = Data(
                currentDateTime,
                value,
                if (!insulinTake.text.toString()
                        .equals("")
                ) (insulinTake.text.toString()).toInt() else null,
                hasMapBooleans.getValue(PICK),
                hasMapBooleans.getValue(ALARM),
                if (!chfood.text.toString().equals("")) (chfood.text.toString()).toInt() else null,
                hasMapBooleans.getValue(FOOD)
            )
            controller.insertIntoMeasure(datos)


        }

        builder.setTitle("Guardar")
        builder.setView(view)
        builder.show()
    }

    fun selectedIcon(imageView: ImageView, key: String) {

        if (!hasMapBooleans.getValue(key)) {
            imageView.setColorFilter(Color.YELLOW)
            hasMapBooleans[key] = true
        } else {
            imageView.setColorFilter(Color.GRAY)
            hasMapBooleans[key] = false
        }
    }

    fun backgroundView(glucosa: Int?): Int {
        val configurationModel = ConfiguracionModel(this.context)
        when {
            glucosa!! >= configurationModel.glucoseMax || glucosa < configurationModel.glucoseMin - 10 -> return Color.RED
            glucosa in configurationModel.glucoseMax - 20 until configurationModel.glucoseMax -> return Color.YELLOW
            glucosa in configurationModel.glucoseMin - 10..configurationModel.glucoseMin -> return Color.YELLOW
            else -> return Color.GREEN
        }

    }
}