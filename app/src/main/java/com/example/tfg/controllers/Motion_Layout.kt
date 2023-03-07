package com.example.tfg.controllers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.tfg.R

@SuppressLint("ResourceType")
class Motion_Layout(activity: Activity,val idMotion: Int) {
    var motioLayout: MotionLayout? = null

    init {
        motioLayout= activity.findViewById(idMotion)

    }
}