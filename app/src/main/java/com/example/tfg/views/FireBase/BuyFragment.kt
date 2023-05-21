package com.example.tfg.views.FireBase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tfg.databinding.FlagmentBuyBinding

class BuyFragment : Fragment() {
    var _binding: FlagmentBuyBinding? = null
    val binding get() = _binding!!
    lateinit var valueLect: TextView
    lateinit var valueAplic: TextView
    lateinit var valueFPack: TextView
    lateinit var total: TextView
    private val lectorValue = 45
    private val aplicatorValue = 10
    private val fullPackValue = 50
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FlagmentBuyBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        valueLect = binding.numLector
        valueAplic = binding.numAplic
        valueFPack = binding.numFPack
        total = binding.totalBuy
        val buttonMinLect = binding.minusButtonLect
        val buttonPlusLect = binding.plusButtonLact
        val buttonMinAplic = binding.minusButtonAplic
        val buttonPlusAplic = binding.plusButtonAplic
        val buttonMinFPack = binding.minusButtonFPack
        val buttonPlusFPack = binding.plusButtonFPack
        buttonMinLect.setOnClickListener {
            if (Integer.parseInt(valueLect.text.toString()) > 0) {
                valueLect.text = (Integer.parseInt(valueLect.text.toString()) - 1).toString()
            }
            refreshValues()

        }
        buttonMinAplic.setOnClickListener {
            if (Integer.parseInt(valueAplic.text.toString()) > 0) {
                valueAplic.text = (Integer.parseInt(valueAplic.text.toString()) - 1).toString()
            }
            refreshValues()

        }
        buttonMinFPack.setOnClickListener {
            if (Integer.parseInt(valueFPack.text.toString()) > 0) {

                valueFPack.text = (Integer.parseInt(valueFPack.text.toString()) - 1).toString()
            }
            refreshValues()

        }
        buttonPlusLect.setOnClickListener {

            valueLect.text = (Integer.parseInt(valueLect.text.toString()) + 1).toString()
            refreshValues()

        }
        buttonPlusAplic.setOnClickListener {
            valueAplic.text = (Integer.parseInt(valueAplic.text.toString()) + 1).toString()
            refreshValues()

        }

        buttonPlusFPack.setOnClickListener {
            valueFPack.text = (Integer.parseInt(valueAplic.text.toString()) + 1).toString()
            refreshValues()

        }
    }

    private fun refreshValues() {
        val total = "${
            (Integer.parseInt(valueAplic.text.toString()) * aplicatorValue) + (Integer.parseInt(
                valueLect.text.toString()
            ) * lectorValue) + (Integer.parseInt(valueFPack.text.toString()) * fullPackValue)
        }"
        this.total.text = total
    }

}