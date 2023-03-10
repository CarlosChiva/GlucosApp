package com.example.tfg.views

import android.app.AlertDialog
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
import com.example.tfg.R
import com.example.tfg.controllers.ControllerMotionLayout
import com.example.tfg.models.ConfiguracionModel
import com.example.tfg.models.EnumActivitys
import com.example.tfg.databinding.FragmentConfigurationBinding
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
    ): View? {

        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //-------------------------Inicialize the ConfigurationModel to load the values recorded before
        configuration = ConfiguracionModel(this.context!!)
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
//inicializacion de hasmap para direcciones de nav y inicializacion de Controller para este fragment
        val hasMap: HashMap<String, Int> = hashMapOf()
        hasMap["configuration"] = R.id.action_ConfigurationFragment_self
        hasMap["stadistics"] = R.id.action_ConfigurationFragment_to_stadisticsFragment
        hasMap["historical"] = R.id.action_ConfigurationFragment_to_historicalFragment
        hasMap["measure"] = R.id.action_ConfigurationFragment_to_measureFragment
        val motionLayout: MotionLayout = view.findViewById(R.id.motion)
        val controllerMotionLayout =
            ControllerMotionLayout(motionLayout, findNavController(), hasMap)
//-------------------Inicialize the values of components based to data recorded before whith ConfigurationModel
        glucMin.text = configuration!!.glucosaMinima.toString()
        glucMax.text = configuration!!.glucosaMaxima.toString()
        alarm.text = configuration!!.alarma.toString()

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
            has.put("glucMax", glucMax.text.toString())
            has.put("glucMin", glucMin.text.toString())
            has.put("alarm", alarm.text.toString())
            alertDialog(has)
        }
        //------------------------------------- Method fo Seekbar to setter the value of cards views

    }

    private fun changeRatingOfSeekBar(enumActivitys: EnumActivitys) {
        rating.max = 0
        rating.min = 0
        var valuesRating = valuesOfRating(enumActivitys)
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

    fun valuesOfRating(enumActivitys: EnumActivitys): Array<Int> {
        var number: Array<Int>
        when (enumActivitys) {
            EnumActivitys.GLUCOSAMAXIMACARD -> number = arrayOf(200, 120)
            EnumActivitys.GLUCOSAMINIMACARD -> number = arrayOf(100, 65)
            EnumActivitys.ALARMA -> return arrayOf(10, 1)
            else -> number = arrayOf()
        }
        return number
    }

    //Method to controller when user press back button, begins the activity
    @Deprecated("Deprecated in Java")
    fun onBackPressed() {
        findNavController().navigate(R.id.action_ConfigurationFragment_to_MainFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun alertDialog(has:HashMap<String,String>) {
        val builder = AlertDialog.Builder(
            this.context
        )
        builder.setMessage("Quieres guardar los cambios?")
        val view = layoutInflater.inflate(R.layout.alert_save, null)
        val glucMaxOld = view.findViewById<TextView>(R.id.gluc_max_old)
        glucMaxOld.setText(configuration!!.glucosaMaxima.toString())
        val glucMaxNew = view.findViewById<TextView>(R.id.gluc_max_result)
        glucMaxNew.text = has.getValue("glucMax")
        val glucMinOld = view.findViewById<TextView>(R.id.gluc_min_old)
        glucMinOld.setText(configuration!!.glucosaMinima.toString())
        val glucMinNew = view.findViewById<TextView>(R.id.gluc_min_result)
        glucMinNew.text = has.getValue("glucMin")
        val alarmOld = view.findViewById<TextView>(R.id.alarm_old_value)
        alarmOld.setText(configuration!!.alarma.toString())
        val alarmNew = view.findViewById<TextView>(R.id.alarm_new_value)
        alarmNew.text = has.getValue("alarm")



        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            saveData()

        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
            Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show()
        }
        builder.setTitle("Guardar")
        builder.setView(view)
        builder.show()

    }

    fun saveData() {
        configuration!!.glucosaMaximaSet((glucMax.text as String).toInt())
        configuration!!.glucosaMinimaSet((glucMin.text as String).toInt())
        configuration!!.alarmaSet((alarm.text as String).toInt())
        configuration!!.saveVAlues()
        onBackPressed()
    }
}


