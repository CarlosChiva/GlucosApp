package com.example.tfg.views.FireBase

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.example.tfg.R
import com.example.tfg.models.ConfiguracionModel

class AlertDialogLogin(context: Context) {
    val context: Context?
    private var alertDialog: AlertDialog? = null
    init {

        this.context = context
        createDialog()

    }


    private fun createDialog() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.login_dialog, null)

        val user = view.findViewById<EditText>(R.id.userText)
        val password = view.findViewById<EditText>(R.id.passwordText)
        val seekbar = view.findViewById<TextView>(R.id.registrer)
        val buttonLogin = view.findViewById<Button>(R.id.buttonLogin)

        val builder = AlertDialog.Builder(context)
            .setView(view)

        alertDialog = builder.create()
        alertDialog?.show()

        buttonLogin.setOnClickListener {
            // Aquí puedes realizar las acciones que deseas al hacer clic en el botón
            // Por ejemplo, imprimir un mensaje
            println("Clicked buttonLogin")
        }
    }

}