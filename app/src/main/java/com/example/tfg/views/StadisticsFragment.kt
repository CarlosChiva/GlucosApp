package com.example.tfg.views

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.controllers.ControllerMotionLayout
import com.example.tfg.controllers.SQLController
import com.example.tfg.controllers.StadisticAdapter
import com.example.tfg.controllers.ViewPagerAdapter
import com.example.tfg.databinding.FragmentStadisticsBinding
import java.time.LocalDate
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mainButton = binding.motion.mainButton
        var bundleextraction = arguments
        var bitmapextraction = bundleextraction?.getParcelable<Bitmap>("image")
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
            ControllerMotionLayout(motionLayout, findNavController(), hasMap, this.context!!)


        val viewPager = binding.viewPager
        val adapter = ViewPagerAdapter(childFragmentManager)
        viewPager.adapter = adapter
        var back = binding.motion.backButton
        back.setOnClickListener{
            println("deddddddd")
        }

//        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
//        recyclerView.adapter = StadisticAdapter(this.context!!, demoList())
    }
//    fun demoList():MutableList<String>{
//        val sqlController= SQLController(this.context!!)
////        val read = LocalDateTime.now().minusMonths(3)
////        return sqlController.readDatesInRange(read, LocalDateTime.now())
//        return sqlController.readDatesAtDay()
//    }
}