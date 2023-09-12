package com.example.tfg.controllers

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.example.tfg.R
import com.google.firebase.auth.FirebaseAuth


class FireBaseController(val context: Context, navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    var nav: NavController? = navController
    private val pushPullDates = PushPullDates(context)
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
      //  pushPullDates.pushConfiguration()
    }
    private fun pullConfiguration() {
        // pushPullDates.pullConfiguration()
    }
    private fun pushDatesMeasure() {}
    private fun pushDatesForeign() {}


    private fun pullDatesMeasure() {}
    private fun pullDatesForeign() {}

}