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
import com.example.tfg.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //inicializacion de hasmap para direcciones de nav y inicializacion de Controller para este fragment
        val motionLayout: MotionLayout = binding.root
        val hasMap: HashMap<String, Int> = hashMapOf()
        hasMap["configuration"] = R.id.action_MainFragment_to_ConfigurationFragment
        hasMap["stadistics"] = R.id.action_MainFragment_to_stadisticsFragment
        hasMap["historical"] = R.id.action_MainFragment_to_HistoricalFragment
        hasMap["measure"] = R.id.action_MainFragment_to_measureFragment
        val controllerLayout =
            ControllerMotionLayout(motionLayout, findNavController(), hasMap)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}