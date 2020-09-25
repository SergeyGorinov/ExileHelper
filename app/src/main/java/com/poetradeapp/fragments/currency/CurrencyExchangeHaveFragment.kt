package com.poetradeapp.fragments.currency

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.poetradeapp.R
import com.poetradeapp.adapters.CurrencyListAdapter
import com.poetradeapp.models.MainViewModel
import kotlinx.android.synthetic.main.fragment_currency_exchange_have.*

class CurrencyExchangeHaveFragment(viewModel: MainViewModel)  : Fragment() {

    private val listAdapter = CurrencyListAdapter(viewModel.getMainData())
    private val listLayoutManager = LinearLayoutManager(context)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_exchange_have, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencyList.apply {
            setHasFixedSize(false)
            setItemViewCacheSize(30)
            layoutManager = listLayoutManager
            adapter = listAdapter
        }
    }
}