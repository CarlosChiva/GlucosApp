package com.example.tfg.views.stadisticsViewsFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfg.controllers.SQLController
import com.example.tfg.databinding.FragmentMonthBinding
import com.example.tfg.databinding.FragmentSixMonthBinding
import com.github.mikephil.charting.charts.LineChart
import java.time.LocalDateTime

class SixMonthFragment : MasterStadistics<FragmentSixMonthBinding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSixMonthBinding {
        return FragmentSixMonthBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.contexto= this.context!!
        this.chart=binding.lineStadistics
        this.pieChart=binding.pieChart
        this.list= demoList()
        this.stadisticsGraphics = makeStadisctic(contexto)    }

    override fun demoList():List<Pair<LocalDateTime, Int>>{
        val sqlController= SQLController(this.context!!)
        val read = LocalDateTime.now().minusMonths(6)
        return sqlController.readDatesInRange(read, LocalDateTime.now())

    }
}