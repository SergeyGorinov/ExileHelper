package com.poetradeapp.fragments.item

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.poetradeapp.R
import kotlinx.android.synthetic.main.fragment_item_exchange_main.*

class ItemsSearchViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ItemsSearchFiltersFragment()
            else -> ItemsSearchFiltersFragment()
        }
    }
}

class ItemExchangeMainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflanter = TransitionInflater.from(requireContext())
        exitTransition = inflanter.inflateTransition(R.transition.fragment_slide)
        enterTransition = inflanter.inflateTransition(R.transition.fragment_slide)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_exchange_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemsSearchMainContainer.adapter = ItemsSearchViewPagerAdapter(requireActivity())
        TabLayoutMediator(itemsSearchTabs, itemsSearchMainContainer) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Filters"
            }
        }.attach()
    }
}