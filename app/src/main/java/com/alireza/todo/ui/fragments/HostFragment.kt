package com.alireza.todo.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.alireza.todo.R
import com.alireza.todo.databinding.HostFragmentBinding
import com.alireza.todo.ui.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class HostFragment : Fragment(R.layout.host_fragment) {

    lateinit var binding:HostFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HostFragmentBinding.bind(view)

        findNavController().enableOnBackPressed(false)
        binding.viewPagerId.adapter = ViewPagerAdapter(childFragmentManager,lifecycle)
        TabLayoutMediator(binding.tabHost,binding.viewPagerId){tab,pos->
            when(pos){
                0 -> tab.text = "Todo"
                1 -> tab.text = "Doing"
                2 -> tab.text = "Done"
            }
        }.attach()

        binding.addTaskFab.setOnClickListener {
            view.findNavController().navigate(R.id.addFragment)
        }




    }


}