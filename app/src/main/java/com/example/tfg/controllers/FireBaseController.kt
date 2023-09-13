package com.example.tfg.controllers

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.tfg.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


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
        db.collection(auth.currentUser!!.email.toString()).document("Measure").get()
            .addOnCompleteListener {

            }
    }
    private fun pushDatesForeign() {}



    private fun pullDatesForeign() {}

}