package com.example.tfg.views.fireBase

import android.app.AlertDialog
import android.content.Context
import android.util.Log

import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import com.example.tfg.R
import com.example.tfg.controllers.FireBaseController
import com.example.tfg.models.ConfiguracionModel
import com.example.tfg.models.EnumActivitys

class AlertDialogLogin(context: Context, findNavController: NavController) {
    val context: Context?
    var nav: NavController? = null
    var configuration = ConfiguracionModel(context)

    //Log.d("","")
    init {

        this.context = context
        nav = findNavController
        createDialog(EnumActivitys.SIGN_IN)

    }

    private fun loginDialog() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.login_dialog, null)
        val email = view.findViewById<EditText>(R.id.userText)
        val password = view.findViewById<EditText>(R.id.passwordText)
        val registrer = view.findViewById<TextView>(R.id.registrer)
        val buttonLogin = view.findViewById<Button>(R.id.buttonLogin)

        val builder = AlertDialog.Builder(context)
            .setView(view)
        val alertDialog: AlertDialog? = builder.create()
        alertDialog?.show()
        email.setText(configuration.userGet())
        password.setText(configuration.passwordGet())
        registrer.setOnClickListener {
            createDialog(EnumActivitys.SIGN_UP)
            alertDialog!!.dismiss()
        }
        buttonLogin.setOnClickListener {
            val autentication = FireBaseController(context!!)
            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                autentication.autentication(
                    email.text.toString(),
                    password.text.toString()
                ) { isAuthenticated ->
                    if (isAuthenticated) {
                        newUser(
                            email.text.toString(),
                            password.text.toString()
                        )
                        alertDialog!!.dismiss()
                        navViews()
                    } else {
                        Toast.makeText(
                            this.context,
                            "There's no user registred",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }

    private fun registrerDialog() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.sing_up, null)

        val email = view.findViewById<EditText>(R.id.useremail)
        val password = view.findViewById<EditText>(R.id.passwordText)
        val passwordRep = view.findViewById<EditText>(R.id.passwordTextRp)
        val registrerButton = view.findViewById<Button>(R.id.buttonRegistrer)

        val builder = AlertDialog.Builder(context)
            .setView(view)

        val alertDialog: AlertDialog? = builder.create()
        alertDialog?.show()
        registrerButton.setOnClickListener {
            if (checkPassword(password.text.toString(), passwordRep.text.toString())) {
                val registrer = FireBaseController(context!!)
                if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                    registrer.registr(
                        email.text.toString(),
                        password.text.toString()
                    ) { registred ->
                        if (registred) {
                            Log.d("Paso:Registred", "Paso 1, usuario registrado")
                            newUser(email.text.toString(), password.text.toString())
                            alertDialog!!.dismiss()
                            Toast.makeText(
                                this.context,
                                "Registred sucessfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            navViews()
                        }

                    }
                }

            } else {
                Toast.makeText(
                    this.context,
                    "Write the password twice to registre",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }


    private fun checkPassword(password1: String, password2: String): Boolean {
        return password1 == password2
    }

    private fun newUser(user: String, password: String) {

        if (!isCurrentUser(user, password)
        ) {
            if (!firstConection()) {
                Log.d("Paso", "Change user second conexion")
                val firebaseOld = FireBaseController(this.context!!)
                firebaseOld.autentication(configuration.userGet(), configuration.passwordGet()) {
                    if (it) {
                        Log.d("Paso:", "Paso 2, usuario nuevo autenticado")
                        firebaseOld.pushAll { savedOld ->
                            if (savedOld) {
                                Log.d("Paso:", "Paso 3, guardado de datos antiguos")
                                autenticationPush(user, password)
                            }
                        }
                    }
                }
            } else {
                Log.d("Paso", "2 Primera conexion")
                autenticationPush(user, password)
            }
        } else {
            Log.d("Paso", "Is current user")
            autenticationPush(user, password)
        }
    }

    private fun autenticationPush(user: String, password: String) {
        configuration.userSet(user)
        configuration.passwordSet(password)
        configuration.saveVAlues()
        val firebase = FireBaseController(this.context!!)
        firebase.autentication(user, password) {
            if (it) {
                firebase.push()
                Log.d("Paso:", "Paso 4, subida de datos del usuario")

            }
        }
    }

    private fun firstConection(): Boolean {
        return configuration.userGet().equals("") && configuration.passwordGet().equals("")
    }

    private fun isCurrentUser(user: String, password: String): Boolean {
        return configuration.userGet().equals(user) && configuration.passwordGet()
            .equals(password)
    }

    private fun navViews() {
        nav!!.navigate(R.id.action_MainFragment_to_viewPagerFirebase)

    }

    private fun createDialog(enumActivitys: EnumActivitys) {
        when (enumActivitys) {
            EnumActivitys.SIGN_IN -> {
                loginDialog()
            }

            EnumActivitys.SIGN_UP -> {
                registrerDialog()
            }

            else -> {}
        }
    }

}