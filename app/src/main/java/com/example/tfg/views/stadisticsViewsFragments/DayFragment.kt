package com.example.tfg.views.stadisticsViewsFragments

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
    }
    fun demoList():MutableList<String>{
        val sqlController= SQLController(this.context!!)
//        val read = LocalDateTime.now().minusMonths(3)
//        return sqlController.readDatesInRange(read, LocalDateTime.now())
        return sqlController.readDatesAtDay()
    }
}