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
import com.example.tfg.databinding.FragmentSixMonthBinding
import com.github.mikephil.charting.charts.LineChart
import java.time.LocalDateTime

class SixMonthFragment : Fragment() {

    private var _binding: FragmentSixMonthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSixMonthBinding.inflate(inflater, container, false)
        return binding.root   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list=demoList()
       val chart: LineChart = binding.lineStadistics
        val pieChart=binding.pieChart
        val stadisticsGraphics=StadisticsGraphics(requireContext(),chart,pieChart,list)

    }
    fun demoList():List<Pair<LocalDateTime, Int>>{
        val sqlController= SQLController(this.context!!)
        val read = LocalDateTime.now().minusMonths(6)
        return sqlController.readDatesInRange(read, LocalDateTime.now())

    }
}