package com.example.tfg.controllers

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.tfg.MainActivity
import com.example.tfg.R
import java.util.*

class NotificationService : BroadcastReceiver() {
    companion object{
        const val NOTIFICATION_ID=1
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        createNotification(context)
    }

    fun createNotification(context: Context?) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag= if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent:PendingIntent=PendingIntent.getActivity(context,0,intent,flag)
        val builder=  NotificationCompat.Builder(context!!.applicationContext,MainActivity.MY_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_delete)
                .setContentTitle("Alarma")
                .setContentText("Ejemplo")
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("aaaaaaaaaaaaa")
                )
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

    val manager=context!!.getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
   manager.notify(NOTIFICATION_ID,builder)
    }
}