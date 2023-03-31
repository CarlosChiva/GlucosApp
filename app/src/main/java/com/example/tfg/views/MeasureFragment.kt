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
            val listValues: List<Pair<LocalDateTime, Int>> = loadValues()
            //   val currentDateTime= LocalDateTime.now().withYear(2022)
            val controller = SQLController(this.context!!)
            controller.insertIntofOREIGNMedida(listValues)
              alertDialog(listValues.get(listValues.size-1).second+10, LocalDateTime.now())
        }


    }

    fun alertDialog(value: Int, currentDateTime: LocalDateTime) {
        val alert = AlertDialogMeasure(this.context!!, value, currentDateTime)
    }

    private fun loadValues(): List<Pair<LocalDateTime, Int>> {
        val sQLController = SQLController(this.context!!)
        val ultimFecha = sQLController.readLastDatesToMeasure()
        val now = LocalDateTime.now()
        val values = mutableListOf<Pair<LocalDateTime, Int>>()
        var dateLoaded = ultimFecha.first.plusMinutes(5)
        var valueLoaded = ultimFecha.second
        var value: Int = 0
        var direccionValues = true
        println("Date loaded in Measure---------------------------------------")
        while (dateLoaded < now) {
            // Generate a random value between 0 and 100
            if (direccionValues) {
                if (valueLoaded <= 180) {
                    value = valueLoaded
                    valueLoaded++
                }

            } else {
                direccionValues = false
            }
            if (!direccionValues) {
                if (valueLoaded >= 80) {
                    value = valueLoaded
                    valueLoaded--
                }

            } else {
                direccionValues = true

            }

            println(dateLoaded)
            values.add(dateLoaded to value)

            dateLoaded = dateLoaded.plusMinutes(5)
        }

        return values
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}