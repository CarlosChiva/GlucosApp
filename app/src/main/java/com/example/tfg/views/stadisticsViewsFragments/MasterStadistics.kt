package com.example.tfg.views.stadisticsViewsFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import java.time.LocalDateTime

abstract class MasterStadistics<B : ViewBinding>() : Fragment() {
   open lateinit var contexto: Context
    open lateinit var list: List<Pair<LocalDateTime, Int>>
    open lateinit var chart: LineChart
    open lateinit var pieChart: PieChart
    open lateinit var stadisticsGraphics: StadisticsGraphics
    protected var _binding: B? = null
    protected val binding get() = _binding!!

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): B
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getBinding(inflater, container)
        return binding.root
    }
    protected fun makeStadisctic(context: Context):StadisticsGraphics{
        return StadisticsGraphics(context, this.chart, this.pieChart, this.demoList())

    }

    abstract override fun onViewCreated(view: View, savedInstanceState: Bundle?)

    protected abstract fun demoList(): List<Pair<LocalDateTime, Int>>

}