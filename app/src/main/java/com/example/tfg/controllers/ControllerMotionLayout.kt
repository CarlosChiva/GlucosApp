package com.example.tfg.controllers

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.NavController
import com.example.tfg.R
import com.example.tfg.databinding.MotionlayoutBinding

class ControllerMotionLayout(

    id: MotionLayout?,
    val nav: NavController,
    val maping: HashMap<String, Int>,

) :
    MotionLayout.TransitionListener {
    var motionLayout: MotionLayout
    var map: HashMap<String, Int>? = null
    val activity = Activity()

    init {
        motionLayout = id!!

        motionLayout.setTransitionListener(this)
        this.map = maping

    }

    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {

    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {

    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int)
// val currentState = motionLayout?.currentState
    {
        when (motionLayout?.currentState) {
            R.id.configurationEnd -> {
                map?.getValue("configuration")?.let { nav.navigate(it) }
            }
            R.id.estadisticsEnd -> {
                map?.getValue("stadistics")?.let { nav.navigate(it) }
            }
            R.id.medidaEnd -> {
                map?.getValue("medida")?.let { nav.navigate(it) }
            }
            R.id.hostoricEnd -> {
                map?.getValue("historical")?.let { nav.navigate(it) }

            }

        }

    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
    }


}
