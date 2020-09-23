package com.poetradeapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SwipePagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = mutableListOf<Fragment>()

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemId(position: Int) = fragments[position].hashCode().toLong()

    override fun containsItem(itemId: Long): Boolean {
        return fragments.contains(itemId)
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)

    }

    fun removeFragment(id: Int) {
        fragments.removeAt(id)
        notifyItemRangeChanged(id, fragments.size)
        notifyDataSetChanged()
    }
}