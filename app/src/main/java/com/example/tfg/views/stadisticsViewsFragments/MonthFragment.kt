package com.example.tfg.views.stadisticsViewsFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.controllers.SQLController
import com.example.tfg.controllers.StadisticAdapter
import com.example.tfg.databinding.FragmentMonthBinding
import com.example.tfg.databinding.FragmentWeekBinding
import com.github.mikephil.charting.charts.LineChart
import java.time.LocalDateTime

class MonthFragment : Fragment() {

    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonthBinding.inflate(inflater, container, false)
        return binding.root   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataPairs = demoList()
        val chart: LineChart = binding.lineStadistics
        val pieChart=binding.pieChart
        val stadisticsGraphics=StadisticsGraphics(requireContext(),chart,pieChart,dataPairs)

    }
    fun demoList():List<Pair<LocalDateTime, Int>>{
        val sqlController= SQLController(this.context!!)
        val read = LocalDateTime.now().minusMonths(1)
        return sqlController.readDatesInRange(read, LocalDateTime.now())

    }
}