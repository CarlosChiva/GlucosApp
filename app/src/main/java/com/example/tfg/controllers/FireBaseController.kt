package com.example.tfg.controllers

import android.content.Context
import android.util.Log
import androidx.navigation.NavController
import com.example.tfg.R
import com.example.tfg.models.ConfiguracionModel
import com.example.tfg.models.Mensage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.runBlocking


class FireBaseController(val context: Context,navController: NavController) {
    val SQLController = SQLController(context)
    val configuration = ConfiguracionModel(context)
    val auth = FirebaseAuth.getInstance()
    var nav: NavController? = navController

    fun autentication(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                auth.currentUser?.reload()?.addOnCompleteListener { reloadTask ->
                    if (reloadTask.isSuccessful){
                        navViews()
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
                    Log.d("registrerrrrrrrrrrr", " Yeeeess")
                } else {
                    Log.d("registrerrrrrrrrrrr", " No")
                }
            }
    }

    fun loadForumCommon(): List<Mensage> {
        var list = listOf<Mensage>()
        var mutable = mutableListOf<Mensage>()
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("commonForum")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Recorre todos los elementos hijos dentro del dataSnapshot
                // Recorre todos los elementos hijos dentro del dataSnapshot
                for (childSnapshot in dataSnapshot.children) {
                    // Obtén los valores de los campos específicos utilizando getValue()
                    val text = childSnapshot.child("text").getValue(String::class.java)
                    mutable.add(convertMensaje(text!!))
                    // Realiza las operaciones necesarias con los datos obtenidos
                    // ...
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja cualquier error que pueda ocurrir al obtener los datos
            }
        })
        list = mutable.toList()
        return list
    }

    private fun convertMensaje(string: String): Mensage {
        var text = string
        var array = text.split("//")
        val sender = array[0]
        val mensage = array[1]
        return Mensage(sender, mensage)

    }

    fun loadPrivateForum(): List<Mensage> {
        var list: List<Mensage> = listOf()
        return list
    }

    private fun navViews() {
        nav!!.navigate(R.id.action_MainFragment_to_viewPagerFirebase)

    }
}