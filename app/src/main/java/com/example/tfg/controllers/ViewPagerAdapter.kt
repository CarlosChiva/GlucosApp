package com.example.tfg.controllers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.example.tfg.views.stadisticsViewsFragments.*

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {


    override fun getCount(): Int =6

    override fun getItem(position: Int): Fragment {
        // Devuelve el Fragment correspondiente a la posición indicada
        when (position) {
            0-> return DayFragment()
            1->  return WeekFragment()
            2->  return MonthFragment()
            3->  return ThreeMonthFragment()
            4->  return SixMonthFragment()
            5->  return StadisticResultFragment();
            else->
                return DayFragment()
        }}
}