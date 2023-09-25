package com.example.tfg.views.fireBase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tfg.R
import com.example.tfg.controllers.FireBaseController
import com.example.tfg.databinding.FragmentFirebaseBinding
import com.example.tfg.models.ConfiguracionModel

class FireBaseFragment : Fragment() {
    private var _binding: FragmentFirebaseBinding? = null
    private val binding get() = _binding!!
    lateinit var configuration: ConfiguracionModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirebaseBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fireBaseController = FireBaseController(this.requireContext())
        configuration = ConfiguracionModel(this.requireContext())
        val loginCard = binding.loginCard
        val registrerCard = binding.registrerCard
        val emailLogin = binding.userMailLogin
        val emailRegistrer = binding.useremailRegistrer
        val passwordLogin = binding.passwordLogin
        val passwordRegistrer = binding.passwordRegistrer
        val passwordRegistrerRep = binding.passwordRegistrerRep
        val loginButton = binding.buttonLogin
        val registrerButton = binding.buttonRegistrer
        val progressBar = binding.progressBar
        emailLogin.setText(configuration.userGet())
        passwordLogin.setText(configuration.passwordGet())
        loginButton.setOnClickListener {
            Log.d("Buttons", "press login button")
            loginCard.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            val autentication = FireBaseController(requireContext())
            if (emailLogin.text.isNotEmpty() && passwordLogin.text.isNotEmpty()) {
                autentication.autentication(
                    emailLogin.text.toString(),
                    passwordLogin.text.toString()
                ) { isAuthenticated ->
                    if (isAuthenticated) {
                        newUser(
                            emailLogin.text.toString(),
                            passwordLogin.text.toString()
                        )
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
        registrerButton.setOnClickListener {
            Log.d("Buttons", "press Registrer button button")
            registrerCard.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            if (checkPassword(
                    passwordRegistrer.text.toString(),
                    passwordRegistrerRep.text.toString()
                )
            ) {
                val registrer = FireBaseController(requireContext())
                if (emailRegistrer.text.isNotEmpty() && passwordRegistrer.text.isNotEmpty()) {
                    registrer.registr(
                        emailRegistrer.text.toString(),
                        passwordRegistrer.text.toString()
                    ) { registred ->
                        if (registred) {
                            Log.d("Paso:Registred", "Paso 1, usuario registrado")
                            newUser(
                                emailRegistrer.text.toString(),
                                passwordRegistrer.text.toString()
                            )
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
                val firebaseOld = FireBaseController(this.requireContext())
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
        val firebase = FireBaseController(this.requireContext())
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
        // nav!!.navigate(R.id.action_MainFragment_to_viewPagerFirebase)

    }

}