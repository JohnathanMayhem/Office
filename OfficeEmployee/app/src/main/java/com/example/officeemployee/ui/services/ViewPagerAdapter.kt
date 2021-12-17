package com.example.officeemployee.ui.services

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.officeemployee.ui.orders.EmergencyFragment
import com.example.officeemployee.ui.services.market.MarketFragment
import com.example.officeemployee.ui.services.planned.PlannedFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> PlannedFragment()
            1 -> MarketFragment()
            2 -> EmergencyFragment()
            else -> Fragment()
        }
    }
}