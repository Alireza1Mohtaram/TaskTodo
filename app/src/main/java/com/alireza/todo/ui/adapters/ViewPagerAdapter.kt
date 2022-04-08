package com.alireza.todo.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alireza.todo.ui.fragments.DoingFragment
import com.alireza.todo.ui.fragments.DoneFragment
import com.alireza.todo.ui.fragments.TodoFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodoFragment()
            1 -> DoingFragment()
            2 -> DoneFragment()
            else -> Fragment()
        }
    }
}