package com.example.tfg.controllers

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.tfg.R
import com.example.tfg.models.Data
import com.example.tfg.models.Foreign
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FireBaseController(val context: Context, navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    var nav: NavController? = navController
    private val pushPullDates = PushPullDates(context)
    private val db = FirebaseFirestore.getInstance()

    private var correctkeys: MutableList<String> = mutableListOf()
    fun autentication(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                auth.currentUser?.reload()?.addOnCompleteListener { reloadTask ->
                    if (reloadTask.isSuccessful) {
                        navViews()
                    } else {
                        Toast.makeText(this.context, "Not user registrated", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    }

    fun registr(email: String, password: String) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navViews()

                }
            }
    }

    fun push() {
        pushConfiguration()
        pushDatesMeasure()
        pushDatesForeign()
    }

    fun pull() {
        pullConfiguration()
        // pullDatesMeasure()
        pullDatesForeign()


    }

    private fun navViews() {
        nav!!.navigate(R.id.action_MainFragment_to_viewPagerFirebase)

    }

    private fun pushConfiguration() {
        db.collection(auth.currentUser!!.email.toString()).document("Configuration").set(
            pushPullDates.pushConfiguration()
        )
    }

    private fun pullConfiguration() {
        db.collection(auth.currentUser!!.email.toString()).document("Configuration").get()
            .addOnCompleteListener {
                val list = listOf(
                    it.result.get("glucoseMax").toString().toInt(),
                    it.result.get("glucoseMin").toString().toInt(),
                    it.result.get("alarm").toString().toInt(),
                    it.result.get("lowInsulin").toString().toInt(),
                    it.result.get("sensitiveFactor").toString().toInt(),
                    it.result.get("ratioInsulin").toString().toInt(),
                )

                pushPullDates.pullConfiguration(list)
            }
    }

    private fun pushDatesMeasure() {
        db.collection(auth.currentUser!!.email.toString()).document("Measure").set(
            pushPullDates.pushDates()
        ).addOnCompleteListener {
        }
    }

    private fun pullDatesMeasure() {
        val dataList = mutableListOf<Data>()
        db.collection(auth.currentUser!!.email.toString()).document("Measure").get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.exists()) {
                    val dataMap = querySnapshot.data
                    val keys = dataMap!!.keys
                    keys.forEach { keyIsInRange(it) }
                    correctkeys.forEach {
                        val value = dataMap[it]

                        if (value is Map<*, *>) {
                            // Acceder a los valores dentro del mapa interno
                            val dateMap = value["date"] as? Map<String, Any>
                            val glucose = value["glucose"].toString().toInt()
                            val pick = value["pick"].toString().toInt()
                            val pickIcon = value["pickIcon"] as? Boolean
                            val alarm = value["alarm"] as? Boolean
                            val CHfood = value["chfood"].toString().toInt()
                            val food = value["food"] as? Boolean

                            val year = dateMap!!["year"].toString().toInt()
                            val monthValue = dateMap["monthValue"].toString().toInt()
                            val dayOfMonth = dateMap["dayOfMonth"].toString().toInt()
                            val hour = dateMap["hour"].toString().toInt()
                            val minute = dateMap["minute"].toString().toInt()

                            val date =
                                LocalDateTime.of(year, monthValue, dayOfMonth, hour, minute)
                            dataList.add(
                                Data(
                                    date,
                                    glucose,
                                    pick,
                                    pickIcon,
                                    alarm,
                                    CHfood,
                                    food
                                )
                            )
                        }
                    }
                    pushPullDates.pullDatesMeasure(dataList)
                }
            }
    }

    private fun pushDatesForeign() {

        val originalMap = pushPullDates.pushDatesForeign()
        for ((month, list) in originalMap) {
            val map = mapOf<String, List<Foreign>>(
                month to list
            )
            Log.d("Map utilizado", "$map")
        writeForeignFirestore(map)
        }

       // writeForeignFirestore(map)
            Log.d("Complete push", "Complete push of foreign")

    }

    private fun writeForeignFirestore(map: Map<String, List<Foreign>>) {
        db.collection(auth.currentUser!!.email.toString()).document("Foreign").set(
            map, SetOptions.merge()
        ).addOnCompleteListener {
            Log.d("ForeignPush", "yes!!!!")
        }
    }

    private fun pullDatesForeign() {}


    private fun keyIsInRange(string: String) {
        //-----------------------------------------------------------------------------------
        // Obtén la fecha actual
        val fechaInicial = LocalDateTime.now().minusMonths(3)

        // Define el formato de la fecha proporcionada
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

        // Convierte la fecha proporcionada a LocalDateTime
        val fechaProporcionada = LocalDateTime.parse(string, formato)

        // Compara la fecha actual con la fecha proporcionada
        if (fechaProporcionada.isAfter(fechaInicial)) {
            Log.d("Fecha correcta para cargar", "$fechaProporcionada------   $string")
            correctkeys.add(string)
        }
        //-------------------------------------------------------------------------------
    }


}