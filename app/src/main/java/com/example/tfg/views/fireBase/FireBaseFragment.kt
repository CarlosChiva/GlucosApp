package com.example.tfg.views.fireBase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tfg.R
import com.example.tfg.controllers.FireBaseController
import com.example.tfg.databinding.FragmentFirebaseBinding

class FireBaseFragment : Fragment() {
    private var _binding: FragmentFirebaseBinding? = null
    private val binding get() = _binding!!
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

    /*
        val pushButton = binding.pushButton
        val pullButton = binding.pullButton
        val backButton = binding.backButton

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFirebase_to_MainFragment)
        }
        pushButton.setOnClickListener {
            fireBaseController.push()
            Toast.makeText(this.context, "push button", Toast.LENGTH_SHORT).show()
        }
        pullButton.setOnClickListener {
            fireBaseController.pull()
            Toast.makeText(this.context, "pull button", Toast.LENGTH_SHORT).show()
        }
  */  }
}