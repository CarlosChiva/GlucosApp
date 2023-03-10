package com.example.tfg.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.controllers.ControllerMotionLayout
import com.example.tfg.controllers.HistoricalAdapter
import com.example.tfg.controllers.SQLController
import com.example.tfg.databinding.FragmentHistoricalBinding
import com.example.tfg.databinding.FragmentMeasureBinding
import com.example.tfg.models.Datos
import java.time.LocalDateTime
import java.util.HashMap


class MeasureFragment : Fragment() {
    private var _binding: FragmentMeasureBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeasureBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//inicializacion de hasmap para direcciones de nav y inicializacion de Controller para este fragment
        val hasMap: HashMap<String, Int> = hashMapOf()
        hasMap["configuration"] = R.id.action_measureFragment_to_ConfigurationFragment
        hasMap["stadistics"] = R.id.action_measureFragment_to_stadisticsFragment
        hasMap["historical"] = R.id.action_measureFragment_to_historicalFragment
        hasMap["measure"] = R.id.action_measureFragment_self
        val motionLayout: MotionLayout = view.findViewById(R.id.motion)
        val controllerMotionLayout =
            ControllerMotionLayout(motionLayout, findNavController(), hasMap)
        //----------------------------------------------------------------------------------------- sql controller
        val currentDateTime = LocalDateTime.now()
        val controller = SQLController(this.context!!)
        val list= listOf(100,200,300,500,50,180,400,30)
        val datos= Datos(currentDateTime, list.get(list.size-1), 5, true, 100, false)
        controller.insertIntofOREIGNMedida(list.subList(0, list.size-2),currentDateTime)
        controller.insertIntoMedida(datos)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}