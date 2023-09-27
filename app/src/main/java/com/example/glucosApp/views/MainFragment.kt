package com.example.glucosApp.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.fragment.findNavController
import com.example.glucosApp.R
import com.example.glucosApp.controllers.ControllerMotionLayout
import com.example.glucosApp.databinding.FragmentMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private var fabFirebase: FloatingActionButton? = null
    private var lowInsulinConfig: FloatingActionButton? = null
    private val binding get() = _binding!!
    private lateinit var hashMap: HashMap<String, Int>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabFirebase = binding.firebaseConection
        lowInsulinConfig = binding.lowInsulin
        fabFirebase!!.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_viewPagerFirebase)
        }
        lowInsulinConfig!!.setOnClickListener {
            AlertDialogLowInsulin(this.requireContext())
        }
        hashMap = loadHasMap()
        val motionLayout: MotionLayout = binding.root
        val controllerLayout =
            ControllerMotionLayout(
                motionLayout,
                findNavController(),
                hashMap,
                this.requireContext()
            )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadHasMap(): HashMap<String, Int> {
        val hasMap: HashMap<String, Int> = hashMapOf()
        hasMap["configuration"] = R.id.action_MainFragment_to_ConfigurationFragment
        hasMap["stadistics"] = R.id.action_MainFragment_to_stadisticsFragment
        hasMap["historical"] = R.id.action_MainFragment_to_HistoricalFragment
        hasMap["measure"] = R.id.action_MainFragment_to_measureFragment
        return hasMap
    }
}