package com.example.tfg.views.FireBase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tfg.databinding.ForumFragmentBinding
import com.example.tfg.models.EnumActivitys

class ForumViewFragment (val enum: EnumActivitys): Fragment() {
    private var _binding: ForumFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var title:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ForumFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         title = binding.title
        initComponents()
        val recycler = binding.recycler

    }
   private fun initComponents(){
       when (enum){
           EnumActivitys.COMMON_FORUM->{
               title.text="Common Forum"
           }
           EnumActivitys.DIRECTFORUM->{
               title.text="Direct Chat"
           }

           else -> {}
       }
   }
}