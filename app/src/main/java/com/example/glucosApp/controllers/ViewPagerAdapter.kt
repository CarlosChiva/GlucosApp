package com.example.glucosApp.controllers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.glucosApp.models.EnumActivitys

import com.example.glucosApp.views.stadisticsViewsFragments.*

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int =6
    override fun getItem(position: Int): Fragment {
        // Devuelve el Fragment correspondiente a la posición indicada
        when (position) {
            0-> return StadisticFragment(EnumActivitys.DAY)
            1->  return StadisticFragment(EnumActivitys.WEEK)
            2->  return StadisticFragment(EnumActivitys.MONTH)
            3->  return StadisticFragment(EnumActivitys.THREEMONTHS)
            4->  return StadisticFragment(EnumActivitys.SIXMONTHS)
            5->  return StadisticResultFragment();
            else->
                return StadisticFragment(EnumActivitys.DAY)
        }
    }
}