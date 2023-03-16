package com.example.tfg.views.stadisticsViewsFragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.controllers.SQLController
import com.example.tfg.controllers.StadisticAdapter
import com.example.tfg.databinding.FragmentDayBinding
import com.example.tfg.models.Painter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class DayFragment : Fragment() {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDayBinding.inflate(inflater, container, false)
        return binding.root   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
               val recyclerView=binding.reciclerDay
               recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
               recyclerView.adapter = StadisticAdapter(this.context!!, demoList())
               val painter=binding.painter











    //
//        val chart: LineChart = binding.lineStadistics
//
//// Configurar las propiedades de la vista de la gráfica
//
//// Configurar las propiedades de la vista de la gráfica
//        chart.xAxis.textSize = 12f
//              chart.axisLeft.textSize = 12f
//        chart.description.isEnabled = false
//        chart.setTouchEnabled(true)
//        chart.isDragEnabled = true
//        chart.setScaleEnabled(true)
//        chart.setPinchZoom(true)
//        chart.setDrawGridBackground(false)
//        chart.setBackgroundColor(Color.WHITE)
//
//// Configurar los datos de la gráfica
//
//// Configurar los datos de la gráfica
//        val entries: MutableList<Entry> = ArrayList()
//        entries.add(Entry(0f,100f))
//        entries.add(Entry(1f, 150f))
//        entries.add(Entry(2f, 120f))
//        entries.add(Entry(3f, 200f))
//        entries.add(Entry(4f, 180f))
//        entries.add(Entry(5f, 220f))
//        entries.add(Entry(6f, 190f))
//        val dataSet = LineDataSet(entries, "Nivel de Glucosa")
//        dataSet.color = Color.RED
//
//        dataSet.setCircleColor(Color.RED)
//        dataSet.circleRadius = 5f
//        dataSet.lineWidth = 2f
//        dataSet.valueTextSize = 10f
//        val lineData = LineData(dataSet)
//
//
//// Agregar los datos de la gráfica a la vista de la gráfica
//        chart.data = lineData
//// Actualizar la vista de la gráfica
//        chart.invalidate()

    }
    fun demoList():MutableList<String>{
        val sqlController= SQLController(requireContext())
     return sqlController.readDatesAtDay()
    }
}