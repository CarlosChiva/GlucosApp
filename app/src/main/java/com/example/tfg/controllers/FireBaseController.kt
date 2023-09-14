package com.example.tfg.controllers

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.tfg.R
import com.example.tfg.models.Data
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FireBaseController(val context: Context, navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    var nav: NavController? = navController
    private val pushPullDates = PushPullDates(context)
    private val db = FirebaseFirestore.getInstance()
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
        pullDatesMeasure()
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
        val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        db.collection(auth.currentUser!!.email.toString()).document("Measure").get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.exists()) {
                    val dataMap = querySnapshot.data
                   // Log.d("querySnapshot size", "${dataMap!!.size}")
                    //Log.d("querySnapshot size", "${dataMap!!.keys}")
                    val keys = dataMap!!.keys
                    for (key in keys) {
                        val value = dataMap[key]

                        if (value is Map<*, *>) {
                            // Acceder a los valores dentro del mapa interno
                            val dateMap = value["date"] as? Map<String, Any>
                            val glucose = value["glucose"].toString().toInt()
                            val pick = value["pick"].toString().toInt()
                            val pickIcon = value["pickIcon"] as? Boolean
                            val alarm = value["alarm"] as? Boolean
                            val CHfood = value["chfood"].toString().toInt()
                            val food = value["food"] as? Boolean
                            Log.d("Variables:","glucose: ${glucose} pick:${pick} pickIcon:${pickIcon} alarm:${alarm} chfood${CHfood} food: ${food}")
                            // Verificar si el mapa "dateMap" es válido
                            if (dateMap != null) {
                                val year = dateMap["year"] as? Int
                                val monthValue = dateMap["monthValue"] as? Int
                                val dayOfMonth = dateMap["dayOfMonth"] as? Int
                                val hour = dateMap["hour"] as? Int
                                val minute = dateMap["minute"] as? Int

                                if (year != null && monthValue != null && dayOfMonth != null &&
                                    hour != null && minute != null) {
                                    val date = LocalDateTime.of(year, monthValue, dayOfMonth, hour, minute)
                                   Log.d("Clase construida","${Data(date, glucose, pick, pickIcon, alarm, CHfood, food)}")
                                    dataList.add(Data(date, glucose, pick, pickIcon, alarm, CHfood, food))
                                }
                                else{
                                    Log.d("Clase no construida","-------------------------------")
                                }
                            }
                        }
                    }

//                    // Itera sobre los campos del documento
//                    for ((key, value) in dataMap ?: emptyMap()) {
//                        // Verifica si el valor es un mapa (esto puede variar según tus datos)
//                        if (value is Map<*, *>) {
//                            val date = value["date"] as? String
//                            val glucose = value["glucose"] as? Int
//                            val pick = value["pick"] as? Int
//                            val pickIcon = value["pick icon"] as? Boolean
//                            val alarm = value["alarm"] as? Boolean
//                            val CHfood = value["CHfood"] as? Int
//                            val food = value["food"] as? Boolean
//
//                            // Si se encontró la fecha y otros campos necesarios, crea un objeto Data
//                            if (date != null && glucose != null && pick != null && pickIcon != null
//                                && alarm != null && CHfood != null && food != null
//                            ) {
//                                val dateObj =
//                                    LocalDateTime.parse(date,dateFormatter)
//                                dataList.add(
//                                    Data(
//                                        dateObj,
//                                        glucose,
//                                        pick,
//                                        pickIcon,
//                                        alarm,
//                                        CHfood,
//                                        food
//                                    )
//                                )
//                            }
//                        }
//                    }
//
//                    Log.d("DataList", "${dataList.size}")
//
                }
//                pushPullDates.pullDatesMeasure(dataList)
            }
    }

    private fun pushDatesForeign() {}


    private fun pullDatesForeign() {}

}