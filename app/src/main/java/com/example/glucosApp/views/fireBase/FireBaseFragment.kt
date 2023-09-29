package com.example.glucosApp.views.fireBase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.glucosApp.R
import com.example.glucosApp.controllers.FireBaseController
import com.example.glucosApp.databinding.FragmentFirebaseBinding
import com.example.glucosApp.models.ConfiguracionModel
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import com.example.glucosApp.databinding.FragmentFirebaseLoginBinding

class FireBaseFragment : Fragment() {
    private var _binding: FragmentFirebaseBinding? = null
    private val binding get() = _binding!!
    lateinit var configuration: ConfiguracionModel
    private lateinit var progresBarHoriz: ProgressBar
    private lateinit var progresIndicator: TextView
    private lateinit var nav: NavController
    private lateinit var motion: FragmentFirebaseLoginBinding
    private lateinit var loginCard: CardView
    private lateinit var progressBar: ProgressBar
    private lateinit var fadeIn: Animation
    private lateinit var fadeOut: Animation
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirebaseBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuration = ConfiguracionModel(this.requireContext())
        nav = findNavController()
        motion= binding.motionLogin
        loginCard = motion.loginCard
        val backButton=binding.backButton
        val registrerCard = motion.registrerCard
        val emailLogin = motion.userMailLogin
        val emailRegistrer = motion.useremailRegistrer
        val passwordLogin = motion.passwordLogin
        val passwordRegistrer = motion.passwordRegistrer
        val passwordRegistrerRep = motion.passwordRegistrerRep
        val loginButton = motion.buttonLogin
        val registrerButton = motion.buttonRegistrer
        progressBar = motion.progressBar
        progresIndicator = motion.processDescriptor
        progresBarHoriz = motion.progressBarHorizontal
        emailLogin.setText(configuration.userGet())
        passwordLogin.setText(configuration.passwordGet())
        backButton.setOnClickListener {
            navViews()
        }
        fadeIn = AnimationUtils.loadAnimation(this.requireContext(), R.anim.fade_in)
        fadeOut =
            AnimationUtils.loadAnimation(this.requireContext(), R.anim.fade_out)
        loginButton.setOnClickListener {
            Log.d("Paso", "press login button")
            changeVisibilitiesLogin(loginCard)
            val autentication = FireBaseController(requireContext())
            if (emailLogin.text.isNotEmpty() && passwordLogin.text.isNotEmpty()) {
                autentication.autentication(
                    emailLogin.text.toString(),
                    passwordLogin.text.toString()
                ) { isAuthenticated ->
                    if (isAuthenticated) {
                        Log.d("Paso", "User autentificated")
                        updateProcessBar("User Authenticated",20)
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
            Log.d("Paso", "press Registrer button button")
            changeVisibilitiesLogin(registrerCard)
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
                            Log.d("Paso", "New user registred")
                            updateProcessBar("New user registrated",20)

                            newUser(
                                emailRegistrer.text.toString(),
                                passwordRegistrer.text.toString()
                            )
                            navViews()
                        } else {
                            Toast.makeText(
                                this.context,
                                "The user does exist",
                                Toast.LENGTH_SHORT
                            ).show()
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

    private fun changeVisibilitiesLogin(cardView: CardView) {
        Log.d("Paso","change visibility of components")
        cardView.setVisibility(View.INVISIBLE)
        cardView.startAnimation(fadeOut)
        progressBar.setVisibility(View.VISIBLE)
        progressBar.startAnimation(fadeIn)
        progresBarHoriz.setVisibility(View.VISIBLE)
        progresBarHoriz.startAnimation(fadeOut)
        progresIndicator.setVisibility(View.VISIBLE)
        progresIndicator.startAnimation(fadeOut)

    }

    private fun checkPassword(password1: String, password2: String): Boolean {
        return password1 == password2
    }

    private fun newUser(user: String, password: String) {

        if (!isCurrentUser(user, password)
        ) {
            if (!firstConection()) {
                Log.d("Paso", "Begin to save first user dates")
                val firebaseOld = FireBaseController(this.requireContext())
                firebaseOld.autentication(configuration.userGet(), configuration.passwordGet()) {
                    if (it) {
                        updateProcessBar("Saving old dates of old user",20)
                        firebaseOld.pushAll { savedOld ->
                            if (savedOld) {
                                updateProcessBar("Changing user",20)
                                Log.d("Paso:", "Paso 3, guardado de datos antiguos")
                                autenticationPush(user, password)
                            }
                        }
                    }
                }
            } else {
                updateProcessBar("Saving configuration of user",40)
                Log.d("Paso", "First conexion")
                autenticationPush(user, password)
            }
        } else {
            updateProcessBar("Updating your data",40)
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
                Log.d("Paso:", "current user autenticated")
                updateProcessBar("Update Data",20)
                firebase.push()
                Log.d("Paso:Registred", "Subida de datos final y bajada")
                updateProcessBar("Update finish succesfully",20)
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
        nav.navigate(R.id.action_viewPagerFirebase_to_MainFragment)

    }
    private fun updateProcessBar(menssage:String,valueAdd:Int){
        progresBarHoriz.progress+=valueAdd
        progresIndicator.setText(menssage)
    }

}