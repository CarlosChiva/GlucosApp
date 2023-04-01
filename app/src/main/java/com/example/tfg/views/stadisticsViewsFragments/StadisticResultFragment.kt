package com.example.tfg.views.stadisticsViewsFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfg.controllers.SQLController
import com.example.tfg.databinding.FragmentStadisticResultBinding
import com.example.tfg.models.ConfiguracionModel
import com.github.mikephil.charting.charts.LineChart
import java.time.LocalDateTime


class StadisticResultFragment : Fragment() {

    private var _binding: FragmentStadisticResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStadisticResultBinding.inflate(inflater, container, false)
        return binding.root   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val datesGluc_CH=loadArrayGLuc_CH()
        datesGluc_CH.forEach {
            for (i in it.indices step 3)
            println("insulina:  ${it.get(i)}  glucosaInicial: ${it.get(i+1)}   glucosa final:  ${it.get(i+2)}   ")}
        val glucMañana=binding.valGlucPromMa
        glucMañana.text=loadGlucMorning().toString()
        val insulLenRegis=binding.valInsulRegistrada
        val insulLenRecom=binding.valdosisLenRecomendada
        val ratioRap=binding.ratioInsulRapida
        val insulRapRecom=binding.insulRapidaRecomendada
        var configurationModel=ConfiguracionModel(this.context!!)
        insulLenRegis.text= configurationModel.lowInsulin.toString()

    }
    fun loadGlucMorning():Int{
        val sqlController= SQLController(this.context!!)
        return sqlController.readAvgMorning()

    }
    fun demoList():List<Pair<LocalDateTime, Int>>{
        val sqlController= SQLController(this.context!!)
        val read = LocalDateTime.now().minusMonths(6)
        return sqlController.readDatesInRange(read, LocalDateTime.now())

    }
    fun loadArrayGLuc_CH():List<Array<Int>>{
        val sqlController= SQLController(this.context!!)
        return sqlController.getInsuln_CH()

    }
}