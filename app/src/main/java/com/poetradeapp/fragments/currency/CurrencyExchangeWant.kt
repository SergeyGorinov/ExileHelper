package com.poetradeapp.fragments.currency

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.poetradeapp.R
import com.poetradeapp.adapters.CurrencyListAdapter
import com.poetradeapp.models.MainViewModel
import kotlinx.android.synthetic.main.fragment_currency_exchange_want.*

class CurrencyExchangeWant : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_exchange_want, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencyList.apply {
            setHasFixedSize(false)
            setItemViewCacheSize(50)
            layoutManager = LinearLayoutManager(activity)
            adapter = CurrencyListAdapter(viewModel.getMainData(), true)
        }
    }
}