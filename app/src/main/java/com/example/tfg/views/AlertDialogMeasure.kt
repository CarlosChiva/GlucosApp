package com.example.tfg.views

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.*
import com.example.tfg.R
import com.example.tfg.controllers.SQLController
import com.example.tfg.models.ConfiguracionModel
import com.example.tfg.models.Datos
import com.google.android.material.imageview.ShapeableImageView
import java.time.LocalDateTime

class AlertDialogMeasure(context: Context, values: List<Int>) {
    //----------------------------------------------------------------------------------------- sql controller
    var currentDateTime: LocalDateTime
    var controller: SQLController
    val values: List<Int>
    val context: Context
    var hasMapBooleans: HashMap<String, Boolean> = hashMapOf()
    val ALARM = "alarm"
    val PICK = "pick"
    val FOOD = "food"

    init {
        this.currentDateTime = LocalDateTime.now()
        this.controller = SQLController(context)
        this.values = values
        this.context = context
        createDialog()
        hasMapBooleans.put(ALARM, false)
        hasMapBooleans.put(PICK, false)
        hasMapBooleans.put(FOOD, false)
    }


    private fun createDialog() {

        val builder = AlertDialog.Builder(
            context
        )
        builder.setMessage("Algo que configurar?")
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.measure_alert, null)

        val gluc = view.findViewById<TextView>(R.id.glucValue)
        gluc.text = values.get(values.size - 1).toString()

        val date = view.findViewById<TextView>(R.id.dateOfMedicion)
        date.text =
            "${currentDateTime.dayOfWeek.value}-${currentDateTime.month.value}-${currentDateTime.year} ${currentDateTime.hour}:${currentDateTime.minute}:${currentDateTime.second} "

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
        signalVAlue.setBackgroundColor(backgroundView(values.get(values.size - 1)))


//----------------------------------------------------------------------------------------------------------

        val insulinTake = view.findViewById<EditText>(R.id.insulinTake)
        val chfood = view.findViewById<EditText>(R.id.chOfFood)

        //--------------------------------------------------------------------------------------------------


        builder.setPositiveButton(android.R.string.ok)
        { dialog, which ->
            val datos = Datos(
                currentDateTime,
                values.get(values.size - 1),
                (insulinTake.text.toString()).toInt(),
                hasMapBooleans.getValue(ALARM),
                (chfood.text.toString()).toInt(),
                hasMapBooleans.getValue(FOOD)
            )
            controller.insertIntofOREIGNMedida(values.subList(0, values.size - 2), currentDateTime)
            controller.insertIntoMedida(datos)

        }

        builder.setTitle("Guardar")
        builder.setView(view)
        builder.show()
    }

    fun selectedIcon(imageView: ImageView, key: String) {

        if (!hasMapBooleans.getValue(key)) {
            imageView.setColorFilter(Color.YELLOW)
            hasMapBooleans.put(key, true)
        } else {
            imageView.setColorFilter(Color.GRAY)
            hasMapBooleans.put(key, false)
        }
    }

    fun backgroundView(glucosa: Int?): Int {
        val configurationModel = ConfiguracionModel(this.context)
        when {
            glucosa!! >= configurationModel.glucosaMaxima || glucosa < configurationModel.glucosaMinima - 10 -> return Color.RED
            glucosa in configurationModel.glucosaMaxima - 20 until configurationModel.glucosaMaxima -> return Color.YELLOW
            glucosa in configurationModel.glucosaMinima - 10..configurationModel.glucosaMinima -> return Color.YELLOW
            else -> return Color.GREEN
        }

    }

}