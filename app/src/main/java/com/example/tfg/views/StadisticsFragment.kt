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
import java.util.HashMap


class StadisticsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stadistics, container, false)
    }

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
    }
}