package com.example.tfg.views.FireBase

import android.app.AlertDialog
import android.content.Context

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
        val registrerButton = view.findViewById<Button>(R.id.buttonsignUp)

        val builder = AlertDialog.Builder(context)
            .setView(view)

        val alertDialog: AlertDialog? = builder.create()
        alertDialog?.show()
        registrerButton.setOnClickListener {
            val registrer = FireBaseController(context!!)
            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                registrer.registr(email.text.toString(), password.text.toString()) { registred ->
                    if (registred) {
                        newUser(email.text.toString(), password.text.toString())
                        alertDialog!!.dismiss()
                        Toast.makeText(this.context, "Registred sucessfully", Toast.LENGTH_SHORT)
                            .show()
                        navViews()
                    }

                }


            }

        }
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

    private fun newUser(user: String, password: String) {

        if (!configuration.userGet().equals(user) || !configuration.passwordGet()
                .equals(password)
        ) {
            val firebaseOld = FireBaseController(this.context!!)
            firebaseOld.autentication(configuration.userGet(), configuration.passwordGet()) {
                if (it) {
                    firebaseOld.pushAll() { savedOld ->
                        if (savedOld) {
                            //necesito que pare la ejecucion para hacer tod el push antes de las siguientes lineas
                            configuration.userSet(user)
                            configuration.passwordSet(password)
                            configuration.saveVAlues()
                            val firebase = FireBaseController(this.context)
                            firebase.autentication(user, password) {
                                if (it) {
                                    firebase.push()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}