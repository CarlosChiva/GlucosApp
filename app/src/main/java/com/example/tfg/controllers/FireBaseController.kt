package com.example.tfg.controllers

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.tfg.R
import com.example.tfg.models.Data
import com.example.tfg.models.Foreign
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FireBaseController(val context: Context, navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    var nav: NavController? = navController
    private val pushPullDates = PushPullDates(context)
    private val db = FirebaseFirestore.getInstance()
    private val FOREIGN = "Foreign"
    private val MEASURE = "Measure"
    private val CONFIGURATION = "Configuration"
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
        // pushConfiguration()
        //pushDatesMeasure()
        pushDatesForeign()
    }

    fun pull() {
        //  pullConfiguration()
        //pullDatesMeasure()
        //pullDatesForeign()

    }

    private fun navViews() {
        nav!!.navigate(R.id.action_MainFragment_to_viewPagerFirebase)

    }

    private fun lastDate(): Int {
        val localDateTime = LocalDateTime.now().monthValue
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection(auth.currentUser!!.email.toString()).document(FOREIGN)

        try {
            val task: com.google.android.gms.tasks.Task<DocumentSnapshot> = documentReference.get()
            Tasks.await(task)
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val dataMap = document.data
                    val key = dataMap!!.keys
                    var monthValue = 0

                    key.forEach {
                        val valueMonth = it[0].toString().toInt()
                        if (valueMonth == localDateTime) {
                            monthValue = valueMonth
                            Log.d("Key:1", monthValue.toString())
                        }
                    }
                    Log.d("Key:2", monthValue.toString())
                    return monthValue
                }
            }
        } catch (e: Exception) {
            e.cause
        }
        return 0
    }

    //----------------------------------------------------Push methods----------------------------------------
    private fun pushConfiguration() {
        db.collection(auth.currentUser!!.email.toString()).document(CONFIGURATION).set(
            pushPullDates.pushConfiguration()
        )
    }

    private fun pushDatesMeasure() {
        db.collection(auth.currentUser!!.email.toString()).document(MEASURE).set(
            pushPullDates.pushDates(), SetOptions.merge()
        ).addOnCompleteListener {
        }
    }

    private fun pushDatesForeign() {
        val originalMap = pushPullDates.pushDatesForeign()
        db.collection(auth.currentUser!!.email.toString()).document(FOREIGN).set(
            originalMap, SetOptions.merge()
        ).addOnCompleteListener {
            Log.d("Write succesfuly", FOREIGN)

        }
    }

    //----------------------------------------Pull methods----------------------------------------
    private fun pullConfiguration() {
        db.collection(auth.currentUser!!.email.toString()).document(CONFIGURATION).get()
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


    private fun pullDatesMeasure() {
        delTableMeasure()
        pullMeasures()
    }

    private fun delTableMeasure() {}
    private fun pullMeasures() {
        val dataList = mutableListOf<Data>()
        var correctKeys = mutableListOf<String>()
        db.collection(auth.currentUser!!.email.toString()).document(MEASURE).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.exists()) {
                    val dataMap = querySnapshot.data
                    val keys = dataMap!!.keys
                    correctKeys = keyIsInRange(keys)
                    if (correctKeys.isNotEmpty()) {
                        correctKeys.forEach {
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

                    }
                }
            }
        pushPullDates.pullDatesMeasure(dataList)
    }


    private fun pullDatesForeign() {
        delTableForeign()
        pullForeign()
    }

    private fun delTableForeign() {}
    private fun pullForeign() {

        val dataList = mutableListOf<Foreign>()
        val currentTime = LocalDateTime.now()
        val monthago = LocalDateTime.now().minusMonths(3)
        db.collection(auth.currentUser!!.email.toString()).document(FOREIGN).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.exists()) {
                    val dataMap = querySnapshot.data!!
                    for ((_, foreignList) in dataMap) {
                        for (foreign in foreignList as List<Foreign>) {
                            if (foreign.date.isAfter(monthago) && foreign.date.isBefore(currentTime)) {
                                dataList.add(foreign)
                            }
                        }
                    }
                }
                pushPullDates.pullForeign(dataList)
            }
        Log.d("Pull  Foreign", "$dataList")
    }


    private fun keyIsInRange(mutableSet: MutableSet<String>): MutableList<String> {
        var correctKeys = mutableListOf<String>()
        //-----------------------------------------------------------------------------------
        // Obtén la fecha actual
        val fechaInicial = LocalDateTime.now().minusMonths(3)
        // Define el formato de la fecha proporcionada
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        mutableSet.forEach {
            // Convierte la fecha proporcionada a LocalDateTime
            val fechaProporcionada = LocalDateTime.parse(it, formato)

            // Compara la fecha actual con la fecha proporcionada
            if (fechaProporcionada.isAfter(fechaInicial)) {
                Log.d("Fecha correcta para cargar", "$fechaProporcionada------   $it")
                correctKeys.add(it)
            }
        }
        return correctKeys
        //-------------------------------------------------------------------------------
    }


}