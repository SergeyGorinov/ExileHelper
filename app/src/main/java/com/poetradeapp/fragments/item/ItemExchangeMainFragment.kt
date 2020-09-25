package com.poetradeapp.fragments.item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.poetradeapp.R
import com.poetradeapp.MainActivity
import com.poetradeapp.models.MainViewModel

class ItemExchangeMainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private val itemExchangeFragmentFilters = ItemExchangeFiltersFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_exchange_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context as MainActivity).supportFragmentManager
            .beginTransaction()
            .add(R.id.itemExchangeContainer, itemExchangeFragmentFilters)
            .commit()
    }
}