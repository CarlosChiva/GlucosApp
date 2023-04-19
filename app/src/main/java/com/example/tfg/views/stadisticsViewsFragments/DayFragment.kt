package com.example.tfg.views.stadisticsViewsFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfg.controllers.SQLController
import com.example.tfg.databinding.FragmentDayBinding
import java.time.LocalDateTime

class DayFragment : MasterStadistics<FragmentDayBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDayBinding {
        return FragmentDayBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.contexto= this.context!!
        this.chart=binding.lineStadistics
        this.pieChart=binding.pieChart
        this.list= demoList()
        this.stadisticsGraphics = makeStadisctic(contexto)
    }

    override fun demoList(): List<Pair<LocalDateTime, Int>> {
        val sqlController = SQLController(contexto)
        return sqlController.readDatesAtDay()
    }


}