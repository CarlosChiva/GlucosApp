package com.example.glucosApp.views

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.fragment.findNavController
import com.example.glucosApp.R
import com.example.glucosApp.controllers.ControllerMotionLayout
import com.example.glucosApp.models.ConfiguracionModel
import com.example.glucosApp.models.EnumActivitys
import com.example.glucosApp.databinding.FragmentConfigurationBinding
import java.util.HashMap

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ConfigurationFragment : Fragment() {
    lateinit var rating: SeekBar
    lateinit var previewValue: TextView
    var value: Int = 0
    lateinit var glucMin: TextView
    lateinit var glucMax: TextView
    lateinit var alarm: TextView
    private var _binding: FragmentConfigurationBinding? = null
    private var configuration: ConfiguracionModel? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainButton = binding.motion.mainButton
        val bundleextraction = arguments
        val bitmapextraction = bundleextraction?.getParcelable<Bitmap>("image")
        mainButton.setImageDrawable(BitmapDrawable(resources, bitmapextraction))
        //-------------------------Inicialize the ConfigurationModel to load the values recorded before
        configuration = ConfiguracionModel(this.requireContext())
        //-------------------------------------------------Inicialize the components of view
        glucMin = binding.glucMin
        glucMax = binding.glucMaxim
        alarm = binding.alarm
        rating = binding.seekbar
        previewValue = binding.previewValue
        val cardMAx = binding.cardMax
        val cardMin = binding.cardMin
        val cardAlarm = binding.cardAlarm
        val cardRoot = binding.cardRoot
        val saveButton = binding.saveButton

        val hasMap: HashMap<String, Int> = hashMapOf()
        hasMap["configuration"] = R.id.action_ConfigurationFragment_self
        hasMap["stadistics"] = R.id.action_ConfigurationFragment_to_stadisticsFragment
        hasMap["historical"] = R.id.action_ConfigurationFragment_to_historicalFragment
        hasMap["measure"] = R.id.action_ConfigurationFragment_to_measureFragment
        hasMap["main"]=R.id.action_ConfigurationFragment_to_Main
        val motionLayout: MotionLayout = view.findViewById(R.id.motion)
        ControllerMotionLayout(motionLayout, findNavController(), hasMap,this.requireContext())
        val backButton= binding.motion.backButton
        backButton.setOnClickListener {
                findNavController().navigate(R.id.action_ConfigurationFragment_to_Main)
            }

//-------------------Inicialize the values of components based to data recorded before whith ConfigurationModel
        glucMin.text = configuration!!.glucosaMinimaGet().toString()
        glucMax.text = configuration!!.glucosaMaximaGet().toString()
        alarm.text = configuration!!.alarmaGet().toString()

//---------------------------------------------------------------------------Listener of the differents card views
        cardRoot.setOnClickListener {
            rating.visibility = View.INVISIBLE
            previewValue.text = ""

        }
        cardMAx.setOnClickListener {
            changeRatingOfSeekBar(EnumActivitys.GLUCOSAMAXIMACARD)
            seekbarChange(glucMax)
        }
        cardMin.setOnClickListener {
            changeRatingOfSeekBar(EnumActivitys.GLUCOSAMINIMACARD)
            seekbarChange(glucMin)
        }
        cardAlarm.setOnClickListener {
            changeRatingOfSeekBar(EnumActivitys.ALARMA)
            seekbarChange(alarm)
        }
        saveButton.setOnClickListener {
            val has: HashMap<String, String> = hashMapOf()
            has["glucMax"] = glucMax.text.toString()
            has["glucMin"] = glucMin.text.toString()
            has["alarm"] = alarm.text.toString()
            alertDialog(has)
        }
        //------------------------------------- Method fo Seekbar to setter the value of cards views

    }

    private fun changeRatingOfSeekBar(enumActivitys: EnumActivitys) {
        rating.max = 0
        rating.min = 0
        val valuesRating = valuesOfRating(enumActivitys)
        rating.max = valuesRating[0]
        rating.min = valuesRating[1]

    }

    private fun seekbarChange(textView: TextView) {
        previewValue.visibility = View.VISIBLE
        rating.progress = textView.text.toString().toInt()
        rating.visibility = View.VISIBLE
        rating.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                previewValue.visibility = View.VISIBLE
                value = progress
                previewValue.text = value.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                textView.setText(value.toString())
                previewValue.visibility = View.INVISIBLE
                value = 0
            }
        })

    }

    private fun valuesOfRating(enumActivitys: EnumActivitys): Array<Int> {
        val number: Array<Int> = when (enumActivitys) {
            EnumActivitys.GLUCOSAMAXIMACARD -> arrayOf(200, 120)
            EnumActivitys.GLUCOSAMINIMACARD -> arrayOf(100, 65)
            EnumActivitys.ALARMA -> return arrayOf(10, 1)
            else -> arrayOf()
        }
        return number
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun alertDialog(has:HashMap<String,String>) {
        val builder = AlertDialog.Builder(
            this.context
        )
        builder.setMessage("Quieres guardar los cambios?")
        val view = layoutInflater.inflate(R.layout.alert_save, null)
        val glucMaxOld = view.findViewById<TextView>(R.id.gluc_max_old)
        glucMaxOld.setText(configuration!!.glucosaMaximaGet().toString())
        val glucMaxNew = view.findViewById<TextView>(R.id.gluc_max_result)
        glucMaxNew.text = has.getValue("glucMax")
        val glucMinOld = view.findViewById<TextView>(R.id.gluc_min_old)
        glucMinOld.setText(configuration!!.glucosaMinimaGet().toString())
        val glucMinNew = view.findViewById<TextView>(R.id.gluc_min_result)
        glucMinNew.text = has.getValue("glucMin")
        val alarmOld = view.findViewById<TextView>(R.id.alarm_old_value)
        alarmOld.setText(configuration!!.alarmaGet().toString())
        val alarmNew = view.findViewById<TextView>(R.id.alarm_new_value)
        alarmNew.text = has.getValue("alarm")



        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            saveData()
            findNavController().navigate(R.id.action_ConfigurationFragment_to_Main)


        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
            Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show()
        }
        builder.setTitle("Guardar")
        builder.setView(view)
        builder.show()

    }

    private fun saveData() {
        configuration!!.glucosaMaximaSet((glucMax.text as String).toInt())
        configuration!!.glucosaMinimaSet((glucMin.text as String).toInt())
        configuration!!.alarmaSet((alarm.text as String).toInt())
        configuration!!.saveVAlues()

    }
}


