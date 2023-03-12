package com.example.tfg.views.stadisticsViewsFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.controllers.SQLController
import com.example.tfg.controllers.StadisticAdapter
import com.example.tfg.databinding.FragmentWeekBinding
import java.time.LocalDateTime


class WeekFragment : Fragment() {

    private var _binding: FragmentWeekBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeekBinding.inflate(inflater, container, false)
        return binding.root   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView=binding.reciclerWeek
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = StadisticAdapter(this.context!!, demoList())
    }
    fun demoList():MutableList<String>{
        val sqlController= SQLController(requireContext())
        val read = LocalDateTime.now().minusDays(7)
        return sqlController.readDatesInRange(read, LocalDateTime.now())

    }
}