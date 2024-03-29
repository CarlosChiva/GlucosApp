package com.example.glucosApp.controllers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.glucosApp.R
import com.example.glucosApp.models.EnumActivitys

class ControllerMotionLayout(
    id: MotionLayout?,
    val nav: NavController,
    maping: HashMap<String, Int>,
    val context: Context

) :
    MotionLayout.TransitionListener {
    var motionLayout: MotionLayout
    var map: HashMap<String, Int>? = null
    var bundle: Bundle

    init {
        motionLayout = id!!
        motionLayout.setTransitionListener(this)
        this.map = maping
        bundle = Bundle()

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
        when (motionLayout?.currentState) {


            R.id.configurationEnd -> {
                map?.getValue("configuration")
                    ?.let { nav.navigate(it, bundleOf(EnumActivitys.CONFIGURATION)) }
            }

            R.id.estadisticsEnd -> {
                map?.getValue("stadistics")
                    ?.let { nav.navigate(it, bundleOf(EnumActivitys.STADISTICS)) }
            }

            R.id.medidaEnd -> {
                map?.getValue("measure")?.let { nav.navigate(it, bundleOf(EnumActivitys.MEASURE)) }
            }

            R.id.hostoricEnd -> {
                map?.getValue("historical")?.let {
                    nav.navigate(it, bundleOf(EnumActivitys.HISTORICAL))
                }

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

    private fun bundleOf(enum: Enum<EnumActivitys>): Bundle {
        when (enum) {
            EnumActivitys.CONFIGURATION -> {
                val drawable = ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_baseline_miscellaneous_services_24
                )
                val bitmap = Bitmap.createBitmap(
                    drawable!!.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bundle.putParcelable("image", bitmap)
                return bundle
            }

            EnumActivitys.STADISTICS -> {

                val drawable =
                    ContextCompat.getDrawable(context, R.drawable.ic_baseline_auto_graph_24)
                val bitmap = Bitmap.createBitmap(
                    drawable!!.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bundle.putParcelable("image", bitmap)
                return bundle
            }

            EnumActivitys.HISTORICAL -> {
                val drawable =
                    ContextCompat.getDrawable(context, R.drawable.ic_baseline_menu_book_24)
                val bitmap = Bitmap.createBitmap(
                    drawable!!.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bundle.putParcelable("image", bitmap)
                return bundle
            }

            EnumActivitys.MEASURE -> {
                val drawable =
                    ContextCompat.getDrawable(context, R.drawable.boton)
                val bitmap = Bitmap.createBitmap(
                    drawable!!.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bundle.putParcelable("image", bitmap)

                return bundle
            }

            else -> {
                val drawable =
                    ContextCompat.getDrawable(context, R.drawable.baseline_radio_button_checked_24)
                val bitmap = Bitmap.createBitmap(
                    drawable!!.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bundle.putParcelable("image", bitmap)

                return bundle
            }
        }
    }


}
