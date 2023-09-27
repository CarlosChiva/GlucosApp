package com.example.tfg.views.stadisticsViewsFragments

import android.content.Context
import android.graphics.Color
import com.example.tfg.models.ConfiguracionModel
import com.example.tfg.models.Foreign
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StadisticsGraphics(
    context: Context?,
    viewColumn: BarChart,
    viewPie: PieChart,
    list: List<Foreign>
) {
    var minimValue = 0
    var maxValue = 0
    var betweenMinMax = 0
    val chart: BarChart
    val pieChart: PieChart
    val lista: List<Foreign>
    val values: ConfiguracionModel

    init {
        values = ConfiguracionModel(context!!)
        this.chart = viewColumn
        this.pieChart = viewPie
        this.lista = list
        drawing()
    }

    fun drawing() {
// Configurar las propiedades de la vista de la gráfica
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)
        chart.setBackgroundColor(Color.WHITE)
        chart.setDrawGridBackground(false)

// Configurar el eje X
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                // Personaliza las etiquetas del eje X según el valor de LocalDateTime
                if (value >= 0 && value < lista.size) {
                    val localDateTime = lista[value.toInt()].date
                    val formatter = DateTimeFormatter.ofPattern("HH") // Puedes ajustar el formato
                    return localDateTime.format(formatter)
                }
                return ""
            }
        }

// Configurar el eje Y izquierdo
        val yAxisLeft = chart.axisLeft
        yAxisLeft.axisMinimum = 40f
        yAxisLeft.axisMaximum = 300f

// Configurar el eje Y derecho (puedes quitarlo si no lo necesitas)
        val yAxisRight = chart.axisRight
        yAxisRight.isEnabled = false

// Configurar los datos de la gráfica de barras
        val entries: MutableList<BarEntry> = ArrayList()
        for (i in lista.indices) {
            val valor = lista[i].glucose.toFloat()
            entries.add(BarEntry(i.toFloat(), valor))
        }

        val dataSet = BarDataSet(entries, "Nivel de Glucosa")
        dataSet.setColor(Color.BLUE) // Puedes personalizar el color de las barras

        val dataSets: MutableList<IBarDataSet> = ArrayList()
        dataSets.add(dataSet)

        val barData = BarData(dataSets)
        barData.barWidth = 0.9f

// Agregar los datos de la gráfica al gráfico de barras
        chart.data = barData

// Actualizar la vista del gráfico de barras
        chart.invalidate()
        //-----------------------------------------------------------------

// Calcular la cantidad de tiempo en que la glucosa ha estado en diferentes rangos
        for (i in 0 until lista.size) {
            val current = lista[i].glucose
            if (current< values.glucosaMinimaGet()){
                minimValue++
            }else if (current >=values.glucosaMinimaGet() &&current >=values.glucosaMaximaGet() ){
                betweenMinMax++
            }else{
                maxValue++
            }
        }

        // Crear una lista de entradas para la gráfica de pastel
        val entriesPie = listOf(
            PieEntry(maxValue.toFloat(), "Por encima de ${values.glucosaMaximaGet()}"),
            PieEntry(
                betweenMinMax.toFloat(),
                "Entre ${values.glucosaMinimaGet()} y ${values.glucosaMaximaGet()}"
            ),
            PieEntry(minimValue.toFloat(), "Por debajo de ${values.glucosaMinimaGet()}")
        )

        // Crear el conjunto de datos y configurar sus propiedades
        val dataSetPie = PieDataSet(entriesPie, "Tiempo en diferentes rangos de glucosa")
        dataSetPie.colors = listOf(Color.RED, Color.GREEN, Color.CYAN)
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