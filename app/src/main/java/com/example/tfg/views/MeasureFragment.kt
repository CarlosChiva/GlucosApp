package com.example.tfg.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.fragment.findNavController
import com.example.tfg.R
import com.example.tfg.controllers.ControllerMotionLayout
import com.example.tfg.controllers.SQLController
import com.example.tfg.databinding.FragmentMeasureBinding
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
        val button = binding.bottomMeasure
        val hasMap: HashMap<String, Int> = hashMapOf()
        hasMap["configuration"] = R.id.action_measureFragment_to_ConfigurationFragment
        hasMap["stadistics"] = R.id.action_measureFragment_to_stadisticsFragment
        hasMap["historical"] = R.id.action_measureFragment_to_historicalFragment
        hasMap["measure"] = R.id.action_measureFragment_self
        val motionLayout: MotionLayout = view.findViewById(R.id.motion)
        ControllerMotionLayout(motionLayout, findNavController(), hasMap)
        button.setOnClickListener {
            val listValues: List<Int> = loadValues()
            val currentDateTime= LocalDateTime.now()

            val controller=SQLController(this.context!!)
            controller.insertIntofOREIGNMedida(listValues.subList(0, listValues.size - 2), currentDateTime)

            alertDialog(listValues.get(listValues.size-1),currentDateTime)
        }


    }

    fun alertDialog(value:Int,currentDateTime:LocalDateTime) {
     val alert= AlertDialogMeasure(this.context!!,value,currentDateTime)
    }

    private fun loadValues(): List<Int> {
        return listOf(100, 150, 300, 170, 140, 70, 127)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}