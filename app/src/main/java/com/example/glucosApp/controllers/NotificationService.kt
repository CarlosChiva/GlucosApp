package com.example.glucosApp.controllers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.glucosApp.MainActivity
import com.example.glucosApp.R
import com.example.glucosApp.models.ConfiguracionModel

class NotificationService : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        createNotification(context)
    }

    fun createNotification(context: Context?) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)
        val builder =
            NotificationCompat.Builder(context!!.applicationContext, MainActivity.MY_CHANNEL_ID)
                .setSmallIcon(R.drawable.boton)
                .setContentTitle("Alarm advice")
                .setContentText("${ConfiguracionModel(context).alarmaGet()}hours have passed since the last measurement ")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Remember to measure back")
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

                .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, builder)
    }
}