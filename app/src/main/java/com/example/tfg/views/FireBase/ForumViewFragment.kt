package com.example.tfg.views.FireBase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.controllers.ChatAdapter
import com.example.tfg.databinding.ForumFragmentBinding
import com.example.tfg.models.EnumActivitys
import com.example.tfg.models.Mensage

class ForumViewFragment(val enum: EnumActivitys) : Fragment() {
    private var _binding: ForumFragmentBinding? = null
    private lateinit var list:List<Mensage>
    private val binding get() = _binding!!
    lateinit var title: TextView
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
        val adapter = ChatAdapter(list, currentUser = "yomismo")
        recycler.adapter= adapter
        recycler.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)

    }

    private fun loadMensagesCommon(): List<Mensage> {
        return listOf(
            Mensage("yomismo", "me duele el brazo cuando me pongo el dispositivo"),
            Mensage("otro", "Eso posiblemente sea Cancer"), Mensage("yomismo", "ok")
        )
    }

    private fun loadPrivateMensages(): List<Mensage> {
        return listOf(
            Mensage("yomismo", "Quiero un dispositivo de Adamantium porque soy Boxeador y sino se me rompe "),
            Mensage("otro", "Si nos pagas mas y encima nos das de lo que te has tomado, puede que podamos encontrar Adamantium, estamos a la espera de que nos conteste Wakanda para empezar a utilizar ese material"), Mensage("yomismo", "ok")
        )
    }

    private fun initComponents() {
        when (enum) {
            EnumActivitys.COMMON_FORUM -> {
                title.text = "Common Forum"
                list=loadMensagesCommon()
            }

            EnumActivitys.DIRECTFORUM -> {
                title.text = "Direct Chat"
                list=loadPrivateMensages()
            }

            else -> {}
        }
    }
}