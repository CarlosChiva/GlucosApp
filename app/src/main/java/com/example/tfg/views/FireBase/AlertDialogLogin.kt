package com.example.tfg.views.FireBase

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.tfg.R
import com.example.tfg.models.EnumActivitys
import com.google.firebase.auth.FirebaseAuth

class AlertDialogLogin(context: Context, findNavController: NavController) {
    val context: Context?
    private var alertDialog: AlertDialog? = null
    var nav: NavController? = null

    init {

        this.context = context
        nav = findNavController
        createDialog(EnumActivitys.SIGN_IN)

    }

    private fun sign_inDialog() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.login_dialog, null)

        val email = view.findViewById<EditText>(R.id.userText)
        val password = view.findViewById<EditText>(R.id.passwordText)
        val registrer = view.findViewById<TextView>(R.id.registrer)
        val buttonLogin = view.findViewById<Button>(R.id.buttonLogin)

        val builder = AlertDialog.Builder(context)
            .setView(view)

        alertDialog = builder.create()
        alertDialog?.show()

        registrer.setOnClickListener {
            createDialog(EnumActivitys.SIGN_UP)
        }
        buttonLogin.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            navViews()
                        } else {
                            showAlert()
                        }
                    }
            }
        }

    }

    private fun sign_upDialog() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.sing_up, null)

        val email = view.findViewById<EditText>(R.id.useremail)
        val password = view.findViewById<EditText>(R.id.passwordText)
        val buttonLogin = view.findViewById<Button>(R.id.buttonsignUp)

        val builder = AlertDialog.Builder(context)
            .setView(view)

        alertDialog = builder.create()
        alertDialog?.show()

        buttonLogin.setOnClickListener {
            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            navViews()
                        } else {
                            showAlert()
                        }
                    }

            }

        }
    }

    private fun showAlert() {

    }

    private fun navViews() {
        nav!!.navigate(R.layout.firebase_fragment)

    }

    private fun createDialog(enumActivitys: EnumActivitys) {
        when (enumActivitys) {
            EnumActivitys.SIGN_IN -> {
                sign_inDialog()
            }

            EnumActivitys.SIGN_UP -> {
                sign_upDialog()
            }

            else -> {}
        }
    }

}