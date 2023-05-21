package com.example.tfg.controllers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.tfg.models.EnumActivitys
import com.example.tfg.views.stadisticsViewsFragments.StadisticFragment
import com.example.tfg.views.stadisticsViewsFragments.StadisticResultFragment

class ViewPagerFireBase(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int =3
    override fun getItem(position: Int): Fragment {
        // Devuelve el Fragment correspondiente a la posición indicada
        when (position) {
            0-> return StadisticFragment(EnumActivitys.DAY)
            1->  return StadisticFragment(EnumActivitys.WEEK)
            2->  return StadisticFragment(EnumActivitys.MONTH)
            else->
                return StadisticFragment(EnumActivitys.DAY)
        }
    }
}