package com.example.tfg.views.stadisticsViewsFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tfg.controllers.SQLController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.example.tfg.databinding.FragmentStadisticBinding
import com.example.tfg.models.EnumActivitys
import com.example.tfg.models.EnumActivitys.*

import java.time.LocalDateTime

class StadisticFragment(val enum: EnumActivitys) : Fragment() {
    lateinit var title: TextView
    lateinit var list: List<Pair<LocalDateTime, Int>>
    lateinit var chart: LineChart
    lateinit var pieChart: PieChart
    lateinit var stadisticsGraphics: StadisticsGraphics
    var _binding: FragmentStadisticBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStadisticBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = binding.titleStadistic
        initComponents()
        this.chart = binding.lineStadistics
        this.pieChart = binding.pieChart
        this.stadisticsGraphics = makeStadisctic(this.context!!)
    }



    private fun makeStadisctic(context: Context): StadisticsGraphics {
        return StadisticsGraphics(context, this.chart, this.pieChart, this.list)

    }

    private fun initComponents() {
        val sqlController = SQLController(this.context!!)

        when (this.enum) {
            DAY -> {
                title.text = "At Day"
                list = sqlController.readDatesAtDay()
            }

            WEEK -> {
                title.text = "At Week"
                list = sqlController.readDatesInRange(
                    LocalDateTime.now().minusDays(7),
                    LocalDateTime.now())
            }

            MONTH -> {
                title.text = "At Month"
                list = sqlController.readDatesInRange(
                    LocalDateTime.now().minusMonths(1),
                    LocalDateTime.now())
            }

            THREEMONTHS -> {
                title.text = "Three Months"
                list = sqlController.readDatesInRange(
                    LocalDateTime.now().minusMonths(3),
                    LocalDateTime.now())
            }

            SIXMONTHS -> {
                title.text = "Six Months"
                list = sqlController.readDatesInRange(
                    LocalDateTime.now().minusMonths(6),
                    LocalDateTime.now())
            }

            else -> {}
        }
    }
}