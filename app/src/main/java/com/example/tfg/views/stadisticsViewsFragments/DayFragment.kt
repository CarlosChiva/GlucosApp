package com.example.tfg.views.stadisticsViewsFragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.controllers.SQLController
import com.example.tfg.controllers.StadisticAdapter
import com.example.tfg.databinding.FragmentDayBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.time.Duration
import java.time.LocalDateTime


class DayFragment : Fragment() {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataPairs = demoList()
        val recyclerView = binding.reciclerDay
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = StadisticAdapter(this.context!!, dataPairs)
        val chart: LineChart = binding.lineStadistics
        val pieChart=binding.pieChart
        val stadisticsGraphics=StadisticsGraphics(requireContext(),chart,pieChart,dataPairs)
//// Configurar las propiedades de la vista de la gráfica
//        chart.xAxis.labelCount = 5
//        chart.xAxis.valueFormatter = IndexAxisValueFormatter(listOf("00:00", "06:00", "12:00", "18:00", "24:00"))
//        chart.xAxis.textSize = 12f
//        chart.axisLeft.textSize = 12f
//        val yAxis = chart.axisLeft
//        yAxis.axisMinimum = 50f
//        yAxis.axisMaximum = 400f
//        chart.description.isEnabled = false
//        chart.setTouchEnabled(true)
//        chart.isDragEnabled = true
//        chart.setScaleEnabled(true)
//        chart.setPinchZoom(true)
//        chart.xAxis.granularity=5f
//        chart.setDrawGridBackground(false)
//        chart.setBackgroundColor(Color.WHITE)
//
//// Configurar los datos de la gráfica
//        val entries: MutableList<Entry> = ArrayList()
//        for (i in 0 until dataPairs.size) {
//            entries.add(Entry(i.toFloat(), dataPairs[i].second.toFloat()))
//        }
//        val dataSet = LineDataSet(entries, "Nivel de Glucosa")
//        dataSet.color = Color.RED
//        dataSet.setCircleColor(Color.RED)
//        dataSet.circleRadius = 1f
//        dataSet.lineWidth = 1f
//        dataSet.valueTextSize = 10f
//        val lineData = LineData(dataSet)
//
//// Agregar los datos de la gráfica a la vista de la gráfica
//        chart.data = lineData
//
//// Actualizar la vista de la gráfica
//        chart.invalidate()
//        //-----------------------------------------------------------------
//       val pieChart=binding.pieChart
//
//// Contadores para los diferentes rangos de glucosa
//        var above180 = 0
//        var below80 = 0
//        var between = 0
//
//// Calcular la cantidad de tiempo en que la glucosa ha estado en diferentes rangos
//        for (i in 0 until dataPairs.size - 1) {
//            val current = dataPairs[i].second
//            val next = dataPairs[i + 1].second
//            val timeDiff = Duration.between(dataPairs[i].first, dataPairs[i + 1].first).toMinutes()
//
//            if (current > 180 && next > 180) {
//                above180 += timeDiff.toInt()
//            } else if (current < 80 && next < 80) {
//                below80 += timeDiff.toInt()
//            } else {
//                between += timeDiff.toInt()
//            }
//        }
//
//// Crear una lista de entradas para la gráfica de pastel
//        val entriesPie = listOf(
//            PieEntry(above180.toFloat(), "Por encima de 180"),
//            PieEntry(between.toFloat(), "Entre 80 y 180"),
//            PieEntry(below80.toFloat(), "Por debajo de 80")
//        )
//
//// Crear el conjunto de datos y configurar sus propiedades
//        val dataSetPie = PieDataSet(entriesPie, "Tiempo en diferentes rangos de glucosa")
//        dataSetPie.colors = listOf(Color.RED, Color.GREEN, Color.BLUE)
//        dataSetPie.sliceSpace = 3f
//        dataSetPie.selectionShift = 5f
//
//// Crear la instancia de la gráfica de pastel
//        val chartPie = binding.pieChart
//
//// Configurar las propiedades de la vista de la gráfica
//        chartPie.setUsePercentValues(false)
//        chartPie.description.isEnabled = false
//        chartPie.setDrawHoleEnabled(true)
//        chartPie.setHoleColor(Color.WHITE)
//        chartPie.setTransparentCircleColor(Color.WHITE)
//        chartPie.setTransparentCircleAlpha(110)
//        chartPie.holeRadius = 58f
//        chartPie.transparentCircleRadius = 61f
//        chartPie.setDrawCenterText(true)
//        chartPie.rotationAngle = 0f
//        chartPie.isRotationEnabled = true
//        chartPie.isHighlightPerTapEnabled= true
//        chartPie.setEntryLabelColor(Color.BLACK)
//        chartPie.setEntryLabelTextSize(12f)
//
//// Agregar los datos de la gráfica a la vista de la gráfica
//        val data = PieData(dataSetPie)
//        chartPie.data = data
//
//// Actualizar la vista de la gráfica
//        chartPie.invalidate()



    }

    fun demoList(): List<Pair<LocalDateTime, Int>> {
        val sqlController = SQLController(requireContext())
        return sqlController.readDatesAtDay()
    }
}