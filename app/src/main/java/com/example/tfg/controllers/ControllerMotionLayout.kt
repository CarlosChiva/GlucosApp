package com.example.tfg.controllers

import android.content.Context
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.NavController
import com.example.tfg.R

class ControllerMotionLayout(
    val context: Context,
    id: View,
    val nav: NavController,
    val enum: EnumActivitys
) :
    MotionLayout.TransitionListener {
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

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int)
    // val currentState = motionLayout?.currentState
    {
        when (motionLayout?.currentState) {
            R.id.configurationEnd -> {
                nav.navigate(getConfiguration()!!)
            }
            R.id.estadisticsEnd -> {
                nav.navigate(getStadistics()!!)
            }
            R.id.medidaEnd -> {
            }
            R.id.hostoricEnd -> {
                nav.navigate(getHistorical()!!)

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

    fun getConfiguration(): Int? {
        return when (enum) {
            EnumActivitys.MAIN -> R.id.action_MainFragment_to_ConfigurationFragment
            EnumActivitys.HISTORICAL -> R.id.action_HistoricalFragment_to_ConfigurationFragment
            EnumActivitys.STADISTICS -> R.id.action_stadisticsFragment_to_ConfigurationFragment

            else -> {
                null
            }
        }
    }

    fun getHistorical(): Int? {
        return when (enum) {
            EnumActivitys.MAIN -> {
                R.id.action_MainFragment_to_HistoricalFragment
            }
            EnumActivitys.STADISTICS -> {
                R.id.action_stadisticsFragment_to_historicalFragment
            }

            else -> {
                null
            }
        }
    }
    fun getStadistics(): Int? {
        return when (enum) {
            EnumActivitys.MAIN -> {
                R.id.action_MainFragment_to_stadisticsFragment
            }
            EnumActivitys.HISTORICAL -> {
                R.id.action_historicalFragment_to_stadisticsFragment
            }

            else -> {
                null
            }
        }
    }
    fun getMedida(): Int? {
        return when (enum) {
            EnumActivitys.MAIN -> {
                R.id.action_MainFragment_to_HistoricalFragment
            }

            else -> {
                null
            }
        }
    }


}
