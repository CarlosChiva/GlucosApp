package com.example.tfg.controllers

import android.content.Context
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.NavController
import com.example.tfg.R

class ControllerMotionLayout(val context: Context, id: View,val nav: NavController) : MotionLayout.TransitionListener {
    var motionLayout: MotionLayout

    init {
        motionLayout = id as MotionLayout
        motionLayout.setTransitionListener(this)

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

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
        // val currentState = motionLayout?.currentState
        when (motionLayout?.currentState) {
            R.id.configurationEnd -> {
                nav.navigate(R.id.action_MainFragment_to_ConfigurationFragment)

            }
            R.id.estadisticsEnd -> {
            }
            R.id.medidaEnd -> {
          }
            R.id.hostoricEnd -> {
                nav.navigate(R.id.action_MainFragment_to_HistoricalFragment)

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
