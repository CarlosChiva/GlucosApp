package com.example.tfg.controllers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.tfg.models.EnumActivitys
import com.example.tfg.views.FireBase.ForumViewFragment

class ViewPagerFireBase(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int =3
    override fun getItem(position: Int): Fragment {
        // Devuelve el Fragment correspondiente a la posición indicada
        when (position) {
            0-> return ForumViewFragment(EnumActivitys.COMMON_FORUM)
            1->  return ForumViewFragment(EnumActivitys.DIRECTFORUM)
            2->  return ForumViewFragment(EnumActivitys.COMMON_FORUM)
            else->
                return ForumViewFragment(EnumActivitys.COMMON_FORUM)
        }
    }
}