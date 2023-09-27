package com.example.glucosApp.views.stadisticsViewsFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.glucosApp.controllers.Analizer
import com.example.glucosApp.databinding.FragmentStadisticResultBinding
import com.example.glucosApp.models.ConfiguracionModel


class StadisticResultFragment : Fragment() {

    private var _binding: FragmentStadisticResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStadisticResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val analizer = Analizer(this.requireContext())
        val glucMañana = binding.valGlucPromMa
        glucMañana.text=analizer.avgGlucMorning.toString()
        val insulLenRegis = binding.valInsulRegistrada
        insulLenRegis.text=analizer.insulLenRecord.toString()
        val insulLenRecom = binding.valdosisLenRecomendada
        insulLenRecom.text = analizer.insulLenRecom.toString()
        val ratioRap = binding.ratioInsulRapida
        ratioRap.text = "${String.format("%.2f",analizer.ratioInsul)} x por Ración"
        var configurationModel = ConfiguracionModel(this.requireContext())
        insulLenRegis.text = configurationModel.lowInsulinGet().toString()
        var sensibilityFactor= binding.sensibilityFactor
        sensibilityFactor.text= "1 Unidad x ${analizer.sensibilityFactor} mg/dl"

    }
}