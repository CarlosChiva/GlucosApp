package com.example.tfg.views.FireBase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tfg.R
import com.example.tfg.controllers.ViewPagerFireBase
import com.example.tfg.databinding.ViewPagerFirebaseBinding

class FireBaseFragment : Fragment() {
    private var _binding: ViewPagerFirebaseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewPagerFirebaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = binding.viewPager
        val adapter = ViewPagerFireBase(childFragmentManager)
        viewPager.adapter = adapter
        val backButton = binding.backButton
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFirebase_to_MainFragment)
        }
    }
}