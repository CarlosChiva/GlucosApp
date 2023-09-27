package com.example.glucosApp.views

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.glucosApp.R
import com.example.glucosApp.controllers.ControllerMotionLayout
import com.example.glucosApp.controllers.ViewPagerAdapter
import com.example.glucosApp.databinding.FragmentStadisticsBinding


class StadisticsFragment : Fragment() {
    private var _binding: FragmentStadisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStadisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainButton = binding.motion.mainButton
        val bundleextraction = arguments
        val bitmapextraction = bundleextraction?.getParcelable<Bitmap>("image")
        mainButton.setImageDrawable(BitmapDrawable(resources, bitmapextraction))
        //inicializacion de hasmap para direcciones de nav y inicializacion de Controller para este fragment
        val hasMap: HashMap<String, Int> = hashMapOf()
        hasMap["configuration"] = R.id.action_stadisticsFragment_to_ConfigurationFragment
        hasMap["historical"] = R.id.action_stadisticsFragment_to_historicalFragment
        hasMap["stadistics"] = R.id.action_stadisticsFragment_self
        hasMap["measure"] = R.id.action_stadisticsFragment_to_measureFragment
        hasMap["main"]=R.id.action_stadisticFragment_to_Main


        val motionLayout: MotionLayout = view.findViewById(R.id.motion)
        val controllerMotionLayout =
            ControllerMotionLayout(motionLayout, findNavController(), hasMap, this.requireContext())


        val viewPager = binding.viewPager
        val adapter = ViewPagerAdapter(childFragmentManager)
        viewPager.adapter = adapter
        val backButton= binding.motion.backButton
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_stadisticFragment_to_Main)
        }

    }

}