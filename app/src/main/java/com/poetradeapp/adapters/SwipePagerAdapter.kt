package com.poetradeapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.poetradeapp.fragments.ItemExchangeFragment
import com.poetradeapp.fragments.currency.CurrencyExchangeFragment

class SwipePagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return CurrencyExchangeFragment()
            1 -> return ItemExchangeFragment()
        }
        return CurrencyExchangeFragment()
    }
}