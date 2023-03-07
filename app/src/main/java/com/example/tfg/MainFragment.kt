package com.example.tfg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.fragment.findNavController
import com.example.tfg.controllers.ControllerMotionLayout
import com.example.tfg.controllers.EnumActivitys
import com.example.tfg.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val motionLayout: MotionLayout = binding.root
        val controllerLayout= ControllerMotionLayout(this.context!!,motionLayout,findNavController(),EnumActivitys.MAIN)
        val fab=binding.mainButton

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}