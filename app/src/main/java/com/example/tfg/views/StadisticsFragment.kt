package com.example.tfg.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.controllers.ControllerMotionLayout
import com.example.tfg.controllers.SQLController
import com.example.tfg.controllers.StadisticAdapter
import com.example.tfg.databinding.FragmentStadisticsBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class StadisticsFragment : Fragment() {
    private var _binding: FragmentStadisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStadisticsBinding.inflate(inflater, container, false)
        return binding.root   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //inicializacion de hasmap para direcciones de nav y inicializacion de Controller para este fragment
        val hasMap: HashMap<String, Int> = hashMapOf()
        hasMap["configuration"] = R.id.action_stadisticsFragment_to_ConfigurationFragment
        hasMap["historical"] = R.id.action_stadisticsFragment_to_historicalFragment
        hasMap["stadistics"] = R.id.action_stadisticsFragment_self
        hasMap["measure"] = R.id.action_stadisticsFragment_to_measureFragment
        val motionLayout: MotionLayout = view.findViewById(R.id.motion)
        val controllerMotionLayout =
            ControllerMotionLayout(motionLayout, findNavController(), hasMap)
        val recyclerView = binding.recyStadis
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
      //  recyclerView.adapter = StadisticAdapter(this.context!!, demoList())
    }
//    fun demoList():MutableList<Int>{
//        val sqlController= SQLController(this.context!!)
//        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
//        val datetime = LocalDateTime.parse("2023-03-11 18:27:20.0", formatter)
//        return sqlController.read2hoursmore(datetime)
//    }
}