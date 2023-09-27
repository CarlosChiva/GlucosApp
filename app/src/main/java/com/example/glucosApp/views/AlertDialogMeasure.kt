package com.example.glucosApp.views

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.widget.*
import com.example.glucosApp.R
import com.example.glucosApp.controllers.NotificationService
import com.example.glucosApp.controllers.NotificationService.Companion.NOTIFICATION_ID
import com.example.glucosApp.controllers.SQLController
import com.example.glucosApp.models.ConfiguracionModel
import com.example.glucosApp.models.Data
import com.google.android.material.imageview.ShapeableImageView
import java.time.LocalDateTime
import java.util.Calendar

class AlertDialogMeasure(context: Context, value: Int, currentDateTime: LocalDateTime) {
    companion object {
        const val MY_CHANNEL_ID = "myChannel"
    }

    //----------------------------------------------------------------------------------------- sql controller
    var controller: SQLController
    val value: Int
    val context: Context
    var hasMapBooleans: HashMap<String, Boolean> = hashMapOf()
    val ALARM = "alarm"
    val PICK = "pick"
    val FOOD = "food"
    val currentDateTime: LocalDateTime
    val hourToAlarm: Int

    init {
        this.currentDateTime = currentDateTime
        this.controller = SQLController(context)
        this.value = value
        this.context = context
        hasMapBooleans[ALARM] = false
        hasMapBooleans[PICK] = false
        hasMapBooleans[FOOD] = false
        hourToAlarm = ConfiguracionModel(context).alarmaGet() * 3600000
        createDialog()

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
            if (hasMapBooleans[ALARM] == true) {
                scheduleNotification()
            }


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

    private fun backgroundView(glucosa: Int?): Int {
        val configurationModel = ConfiguracionModel(this.context)
        when {
            glucosa!! >= configurationModel.glucosaMaximaGet() || glucosa < configurationModel.glucosaMinimaGet() - 10 -> return Color.RED
            glucosa in configurationModel.glucosaMaximaGet() - 20 until configurationModel.glucosaMaximaGet() -> return Color.YELLOW
            glucosa in configurationModel.glucosaMinimaGet() - 10..configurationModel.glucosaMinimaGet() -> return Color.YELLOW
            else -> return Color.GREEN
        }

    }

    private fun scheduleNotification() {
        val intent = Intent(this.context, NotificationService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this.context,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Verifica si la app puede programar alarmas exactas.
        if (alarmManager.canScheduleExactAlarms()) {
            // Programa la alarma exacta.
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis + (ConfiguracionModel(context).alarmaGet() * 3600000),
                pendingIntent
            )
        }
    }


}