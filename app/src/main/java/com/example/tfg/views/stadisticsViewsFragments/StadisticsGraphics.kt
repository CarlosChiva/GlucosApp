package com.example.tfg.views.stadisticsViewsFragments

import android.content.Context
import android.graphics.Color
import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.time.Duration
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.minutes

class StadisticsGraphics(
    context: Context,
    viewLineal: LineChart,
    viewPie: PieChart,
    list: List<Pair<LocalDateTime, Int>>
) {
    val chart: LineChart
    val pieChart: PieChart
    val lista: List<Pair<LocalDateTime, Int>>

    init {
        this.chart = viewLineal
        this.pieChart = viewPie
        this.lista = list
        drawing()
    }

    fun drawing() {
// Configurar las propiedades de la vista de la gráfica
        chart.xAxis.labelCount = lista.size
        chart.xAxis.textSize = 12f
        chart.axisLeft.textSize = 12f
        val yAxis = chart.axisLeft
        yAxis.axisMinimum = 50f
        yAxis.axisMaximum = 400f
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)
        chart.axisRight.isEnabled = false
        chart.xAxis.granularity = 6f
        chart.setDrawGridBackground(false)
        chart.setBackgroundColor(Color.WHITE)
        // Configurar los datos de la gráfica
        val entries: MutableList<Entry> = ArrayList()
        for (i in 0 until lista.size) {
            val valor = lista[i].second.toFloat()
            entries.add(Entry(lista[i].first.minute.toInt().toFloat(), valor))
        }
        val dataSet = LineDataSet(entries, "Nivel de Glucosa")
        dataSet.color = Color.BLACK
        dataSet.setCircleColors(Color.RED)
        dataSet.circleRadius = 1f
        dataSet.lineWidth = 1f
        dataSet.valueTextSize = 10f
        dataSet.fillColor = Color.RED // Agregar el color del relleno
        val lineData = LineData(dataSet)
// Agregar los datos de la gráfica a la vista de la gráfica
        chart.data = lineData

// Actualizar la vista de la gráfica
        chart.invalidate()
        //-----------------------------------------------------------------


        // Contadores para los diferentes rangos de glucosa
        var above180 = 0
        var below80 = 0
        var between = 0

// Calcular la cantidad de tiempo en que la glucosa ha estado en diferentes rangos
        for (i in 0 until lista.size - 1) {
            val current = lista[i].second
            val next = lista[i + 1].second
            val timeDiff = Duration.between(lista[i].first, lista[i + 1].first).toMinutes()

            if (current > 180 && next > 180) {
                above180 += timeDiff.toInt()
            } else if (current < 80 && next < 80) {
                below80 += timeDiff.toInt()
            } else {
                between += timeDiff.toInt()
            }
        }

        // Crear una lista de entradas para la gráfica de pastel
        val entriesPie = listOf(
            PieEntry(above180.toFloat(), "Por encima de 180"),
            PieEntry(between.toFloat(), "Entre 80 y 180"),
            PieEntry(below80.toFloat(), "Por debajo de 80")
        )

        // Crear el conjunto de datos y configurar sus propiedades
        val dataSetPie = PieDataSet(entriesPie, "Tiempo en diferentes rangos de glucosa")
        dataSetPie.colors = listOf(Color.RED, Color.GREEN, Color.BLUE)
        dataSetPie.sliceSpace = 3f
        dataSetPie.selectionShift = 5f

        // Crear la instancia de la gráfica de pastel


// Configurar las propiedades de la vista de la gráfica
        pieChart.setUsePercentValues(false)
        pieChart.description.isEnabled = false
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.WHITE)
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.setDrawCenterText(true)
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(12f)

        // Agregar los datos de la gráfica a la vista de la gráfica
        val data = PieData(dataSetPie)
        pieChart.data = data

// Actualizar la vista de la gráfica
        pieChart.invalidate()
    }
}