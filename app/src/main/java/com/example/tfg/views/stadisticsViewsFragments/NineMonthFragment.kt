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
import com.example.tfg.databinding.FragmentNineMonthBinding
import java.time.LocalDateTime


class NineMonthFragment : Fragment() {

    private var _binding: FragmentNineMonthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNineMonthBinding.inflate(inflater, container, false)
        return binding.root   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView=binding.reciclerNineMonth
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = StadisticAdapter(this.context!!, demoList())
    }
    fun demoList():List<Pair<LocalDateTime, Int>>{
        val sqlController= SQLController(this.context!!)
        val read = LocalDateTime.now().minusMonths(9)
        return sqlController.readDatesInRange(read, LocalDateTime.now())

    }
}