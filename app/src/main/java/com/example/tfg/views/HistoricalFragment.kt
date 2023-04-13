package com.example.tfg.views


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import com.example.tfg.controllers.*
import com.example.tfg.databinding.FragmentHistoricalBinding
import com.example.tfg.models.Datos
import java.util.*

@Suppress("DEPRECATION")
class HistoricalFragment : Fragment() {
    private var _binding: FragmentHistoricalBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoricalBinding.inflate(inflater, container, false)
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
        hasMap["configuration"] = R.id.action_historicalFragment_to_ConfigurationFragment
        hasMap["stadistics"] = R.id.action_historicalFragment_to_stadisticsFragment
        hasMap["measure"] = R.id.action_historicalFragment_to_measureFragment
        hasMap["historical"] = R.id.action_historicalFragment_self
        val motionLayout: MotionLayout = view.findViewById(R.id.motion)
        ControllerMotionLayout(motionLayout, findNavController(), hasMap, this.context!!)
        //Inicialization of reciclerView of the main View of this screen
        val recyclerView = binding.recycler
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = HistoricalAdapter(this.context!!, demoList())
    }

    //Function demo for addd information for testing class adapter
    fun demoList(): MutableList<Datos> {
        val sqlController = SQLController(this.context!!)
        return sqlController.loadDatesMedida()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}