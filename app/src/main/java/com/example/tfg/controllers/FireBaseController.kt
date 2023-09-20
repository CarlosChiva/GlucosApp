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
        pushConfiguration()
        pushDatesMeasure()
        pushDatesForeign()
    }

    fun pull() {
        // clearBd()
        // pullConfiguration()
        pullMeasure()
        //pullForeign()

    }

    private fun clearBd() {
        pushPullDates.clearTables()
    }

    private fun navViews() {
        nav!!.navigate(R.id.action_MainFragment_to_viewPagerFirebase)

    }


    //----------------------------------------------------Push/pull methods of Measure----------------
    private fun pushDatesMeasure() {
        db.collection(auth.currentUser!!.email.toString()).document(MEASURE).set(
            pushPullDates.pushDates(), SetOptions.merge()
        ).addOnCompleteListener {
        }
    }

    private fun pullMeasure() {
        val dataListResult = mutableListOf<Data>()
        db.collection(auth.currentUser!!.email.toString()).document(MEASURE).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.exists()) {
                    val dataMap = querySnapshot.data
                    val keys = dataMap!!.keys
                    val correctKeys = validKeys(keys)
                    if (correctKeys.isNotEmpty()) {
                        correctKeys.forEach {
                            val value = dataMap[it]
                            if (value is ArrayList<*>) {
                                value.forEach { item ->
                                    if (item is HashMap<*, *>) {
                                        val dateMap = item["date"] as? Map<String, Any>
                                        val glucose = item["glucose"].toString().toInt()
                                        val pick = item["pick"].toString().toInt()
                                        val pickIcon = item["pickIcon"] as? Boolean
                                        val alarm = item["alarm"] as? Boolean
                                        val CHfood = item["chfood"].toString().toInt()
                                        val food = item["food"] as? Boolean

                                        val year = dateMap!!["year"].toString().toInt()
                                        val monthValue = dateMap["monthValue"].toString().toInt()
                                        val dayOfMonth = dateMap["dayOfMonth"].toString().toInt()
                                        val hour = dateMap["hour"].toString().toInt()
                                        val minute = dateMap["minute"].toString().toInt()

                                        val date =
                                            LocalDateTime.of(
                                                year,
                                                monthValue,
                                                dayOfMonth,
                                                hour,
                                                minute
                                            )
                                        val data = Data(
                                            date,
                                            glucose,
                                            pick,
                                            pickIcon,
                                            alarm,
                                            CHfood,
                                            food
                                        )
                                        dataListResult.add(
                                            data
                                        )
                                    }

                                }
                            }

                        }
                    }
                }
            }
    }

    fun validKeys(fecha: MutableSet<String>): MutableSet<String> {
        val list = mutableSetOf<String>()
        val currentTime = LocalDateTime.now()
        val monthago = LocalDateTime.now().minusMonths(1).month.value
        val twomonthago = LocalDateTime.now().minusMonths(2).month.value
        val threemonthago = LocalDateTime.now().minusMonths(3).month.value
        val mesActual = currentTime.month.value
        fecha.forEach {
            val mes = it[0].toString().toInt()

            if (mes == mesActual || mes == monthago || mes == twomonthago || mes == threemonthago) {
                list.add(it)
                Log.d("Añadido", it)
            }
        }

        return list
    }


    //-------------------------------Push/Pull methods of Foreign------------------------------------
    private fun pushDatesForeign() {
        val originalMap = pushPullDates.pushDatesForeign()
        db.collection(auth.currentUser!!.email.toString()).document(FOREIGN)
            .set(
                originalMap, SetOptions.merge()
            ).addOnCompleteListener {
                Log.d("Write succesfuly", FOREIGN)

            }
    }


    private fun pullForeign() {
        val foreigList = mutableListOf<Foreign>()

        db.collection(auth.currentUser!!.email.toString()).document(FOREIGN)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.exists()) {
                    val dataMap =
                        querySnapshot.data!! as Map<String, MutableList<Foreign>>
                    if (dataMap.isNotEmpty()) {
                        for ((_, foreignList) in dataMap) {
                            Log.d("ForeignList:", "$foreignList")
                            foreignList.forEach {
                                if (mapToForeignValid(it)) {
                                    foreigList.add(it)
                                }
                            }
                        }
                        Log.d("DataListResultForeign", "$foreigList")
                    }
                }

            }


    }
//        pushPullDates.pullForeign(foreigList)


    fun mapToForeignValid(foreign: Foreign): Boolean {
        val currentTime = LocalDateTime.now()
        val monthago = LocalDateTime.now().minusMonths(3)
        if (foreign.date.isAfter(monthago) && foreign.date.isBefore(
                currentTime
            )
        ) {
            return true
        }
        return false
    }

    //----------------------------------------Push/pull methods of configuration---------------------
    private fun pushConfiguration() {
        db.collection(auth.currentUser!!.email.toString()).document(CONFIGURATION)
            .set(
                pushPullDates.pushConfiguration()
            )
    }

    private fun pullConfiguration() {
        db.collection(auth.currentUser!!.email.toString()).document(CONFIGURATION)
            .get()
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

}