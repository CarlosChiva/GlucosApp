package com.example.tfg.views.stadisticsViewsFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tfg.controllers.Analizer
import com.example.tfg.controllers.SQLController
import com.example.tfg.databinding.FragmentStadisticResultBinding
import com.example.tfg.models.ConfiguracionModel
import com.github.mikephil.charting.charts.LineChart
import java.time.LocalDateTime


class StadisticResultFragment : Fragment() {

    private var _binding: FragmentStadisticResultBinding? = null
    private val binding get() = _binding!!
    val glucMañana: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStadisticResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val analizer = Analizer(this.context!!)
        val glucMañana = binding.valGlucPromMa
        glucMañana.text = analizer.avgGlucMorning.toString()
        val insulLenRegis = binding.valInsulRegistrada
        insulLenRegis.text=analizer.insulLenRecord.toString()
        val insulLenRecom = binding.valdosisLenRecomendada
        insulLenRecom.text = analizer.insulLenRecom.toString()
        val ratioRap = binding.ratioInsulRapida
        ratioRap.text = "${analizer.ratioInsul} x por Racioin"
        var configurationModel = ConfiguracionModel(this.context!!)
        insulLenRegis.text = configurationModel.lowInsulin.toString()
        var sensibilityFactor= binding.sensibilityFactor
        sensibilityFactor.text= "1 Unidad x ${analizer.sensibilityFactor.toString()} mg/dl"

    }



    fun demoList(): List<Pair<LocalDateTime, Int>> {
        val sqlController = SQLController(this.context!!)
        val read = LocalDateTime.now().minusMonths(6)
        return sqlController.readDatesInRange(read, LocalDateTime.now())

    }




}