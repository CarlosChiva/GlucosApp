package com.example.tfg.views.stadisticsViewsFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tfg.controllers.SQLController
import com.example.tfg.databinding.FragmentMonthBinding
import com.example.tfg.databinding.FragmentWeekBinding
import com.github.mikephil.charting.charts.LineChart
import java.time.LocalDateTime


class WeekFragment : MasterStadistics<FragmentWeekBinding>(){
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentWeekBinding {
        return FragmentWeekBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.contexto= this.context!!
        this.chart=binding.lineStadistics
        this.pieChart=binding.pieChart
        this.list= demoList()
        this.stadisticsGraphics = makeStadisctic(contexto)    }


   override fun demoList():List<Pair<LocalDateTime, Int>>{
        val sqlController= SQLController(requireContext())
        val read = LocalDateTime.now().minusDays(7)
        return sqlController.readDatesInRange(read, LocalDateTime.now())

    }
}